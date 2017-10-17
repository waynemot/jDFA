package de.rwth.utils; // Generated package name

import java.util.Vector;

/**
 * This class implements a stop watch which measures the elapsed time
 * in milliseconds.
 *
 * @author <a href="mailto:M.Mohnen@gmx.de">Markus Mohnen</a>
 * @version $Id: Stopwatch.java,v 1.3 2002/09/17 06:53:53 mohnen Exp $
 */
public class Stopwatch  {
  /**
   * The accumulated timings.
   */
  protected Vector timings = null;

  /**
   * The descriptive text of this stopwatch.
   */
  protected String desc = null;
  
  /**
   * The time where the stop watch was started (in milliseconds).  -1
   * iff the stop watch is not started.
   */
  protected long startMillis;

  /**
   * The time where the last split was started (in milliseconds).  -1
   * iff no split was started.
   */
  protected long splitStartMillis;

  /**
   * <code>true</code> iff the stop watch was started more often than
   * stopped.
   */
  protected boolean started;

  /**
   * Creates a new stop watch with descriptive text.
   *
   * @param <code>desc</code> a value of type <code>String</code>
   */
  public Stopwatch(String desc) {
    reset(desc);
  }
  
  /**
   * Creates a new stop watch w/o descriptive text.
   */
  public Stopwatch() { this(""); }

  /**
   * Returns the descriptive text used when creating the stop watch plus. If the
   * description was <code>null</code> this method return the empty string. If it was
   * neither null nor empty, it returns the descriptive text plus space appended.
   *
   * @return a <code>String</code> value
   */
  public String getDescription() {
    return desc;
  }
  
  /**
   * Starts the stop watch with a start text. Starting a stopwatch more than once
   * throws an IllegalArgumentException.
   *
   * @param <code>s</code> a value of type <code>String</code>
   */
  public void start(String s) {
    //System.err.println("start("+s+")");
    startMillis=System.currentTimeMillis();
    if (started) throw new IllegalArgumentException("Stopwatch already started.");
    started=true;
    timings.add(desc+s);
    timings.add("<overall>");
  }
  
  /**
   * Starts the stop watch w/o a start text.
   *
   * @param <code>nil</code> a value of type <code></code>
   */
  public void start() { start(""); }

  /**
   * Starts a new segment in the stopped time with descriptive text. The elapsed
   * time of each segment is measured separately. If the stop watch was not started,
   * it throws an IllegalArgumentException.
   *
   * @param <code>s</code> a value of type <code>String</code>
   */
  public void split(String s) {
    long splitStopMillis=System.currentTimeMillis();
    //System.err.println("split("+s+")");
    if (!started) throw new IllegalArgumentException("Stopwatch not started.");
    if (splitStartMillis==-1) splitStartMillis=startMillis;
    timings.add(desc+s);
    timings.add(new Long(splitStopMillis-splitStartMillis));
    splitStartMillis=splitStopMillis;
  }

  /**
   * Starts a new segment in the stopped time w/o descriptive text.
   */
  public void split() { split(""); }
  
  /**
   * Stops the stop watch and returns the accumulated result. If the stop watch was
   * not started, it throws an IllegalArgumentException.
   *
   * @param <code>s</code> The descriptive text of the last segment 
   */
  public void stop(String s) {
    long stopMillis=System.currentTimeMillis();
    //System.err.println("stop("+s+")");
    if (!started) throw new IllegalArgumentException("Stopwatch not started.");
    if (splitStartMillis!=-1) {
      timings.add(desc+s);
      timings.add(new Long(stopMillis-splitStartMillis));
    }
    timings.set(1,new Long(stopMillis-startMillis));
  }
  
  /**
   * Stops the stop watch and returns the accumulated result.
   */
  public void stop() { stop("rest");}

  /**
   * Resets this stopwatch to its initial state.
   *
   * @param desc a <code>String</code> value
   */
  public void reset(String desc) {
    this.timings=new Vector();
    this.desc=desc;
    if (this.desc==null) this.desc="";
    else if (!this.desc.equals("")) this.desc+=" ";
    this.startMillis=-1;
    this.splitStartMillis=-1;
    this.started=false;
  }
  
  public void reset() { reset(""); }
  
  /**
   * Returns the accumulated result after the watch was stopped.
   *
   * @return a <code>Vector</code> value
   */
  public Vector get() {
    return timings;
  }

  public String toString() {
    return timings.toString();
  }

  /**
   * Mini test environment.
   *
   * @param args a <code>String[]</code> value
   */
  public static void main(String[] args) {
    try {
      Stopwatch stopWatch = new Stopwatch("Test");
      stopWatch.start("testing1");
      Thread.currentThread().sleep(200);
      stopWatch.split("split1");
      Thread.currentThread().sleep(2000);
      stopWatch.split("split2");
      stopWatch.stop("rest");
      System.err.println(stopWatch.get());
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
    }
  }
}
