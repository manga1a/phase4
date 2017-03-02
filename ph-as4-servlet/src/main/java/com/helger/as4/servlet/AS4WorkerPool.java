package com.helger.as4.servlet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.callback.IThrowingRunnable;
import com.helger.commons.concurrent.BasicThreadFactory;
import com.helger.commons.concurrent.ManagedExecutorService;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;

public final class AS4WorkerPool extends AbstractGlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AS4WorkerPool.class);

  private final ExecutorService m_aES;

  @Deprecated
  @UsedViaReflection
  public AS4WorkerPool ()
  {
    m_aES = Executors.newFixedThreadPool (Runtime.getRuntime ().availableProcessors () *
                                          2,
                                          new BasicThreadFactory.Builder ().setDaemon (true)
                                                                           .setNamingPattern ("as4-worker-%d")
                                                                           .build ());
  }

  @Nonnull
  public static AS4WorkerPool getInstance ()
  {
    return getGlobalSingleton (AS4WorkerPool.class);
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction) throws Exception
  {
    s_aLogger.info ("Global AS4 worker queue about to be closed");
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (m_aES);
    s_aLogger.info ("Global AS4 worker queue closed!");
  }

  @Nonnull
  public CompletableFuture <Void> run (@Nonnull final IThrowingRunnable <? extends Throwable> aRunnable)
  {
    return CompletableFuture.runAsync ( () -> {
      try
      {
        aRunnable.run ();
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Error running AS4 runner " + aRunnable, t);
      }
    }, m_aES);
  }

  @Nonnull
  public <T> CompletableFuture <T> supply (@Nonnull final Supplier <T> aSupplier)
  {
    return CompletableFuture.supplyAsync ( () -> {
      try
      {
        return aSupplier.get ();
      }
      catch (final Exception ex)
      {
        s_aLogger.error ("Error running AS4 supplier " + aSupplier, ex);
        return null;
      }
    }, m_aES);
  }
}