package com.SecureAccessPortal.Modal;

public class WorkItemCount {
	private String total;
	private String open;
	private String closed;
	private String in_progress;
	private String completed;
	private String pend_external;
	private String pend_internal;
	private String rejected;
	private String passed;
	private Queue queue;

	/**
	 * @return the queue
	 */
	public Queue getQueue() {
		return queue;
	}

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the open
	 */
	public String getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(String open) {
		this.open = open;
	}

	/**
	 * @return the close
	 */
	public String getClosed() {
		return closed;
	}

	/**
	 * @param close the close to set
	 */
	public void setClosed(String closed) {
		this.closed = closed;
	}

	/**
	 * @return the in_progress
	 */
	public String getIn_progress() {
		return in_progress;
	}

	/**
	 * @param in_progress the in_progress to set
	 */
	public void setIn_progress(String in_progress) {
		this.in_progress = in_progress;
	}

	/**
	 * @return the completed
	 */
	public String getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(String completed) {
		this.completed = completed;
	}

	/**
	 * @return the pend_external
	 */
	public String getPend_external() {
		return pend_external;
	}

	/**
	 * @param pend_external the pend_external to set
	 */
	public void setPend_external(String pend_external) {
		this.pend_external = pend_external;
	}

	/**
	 * @return the pent_internal
	 */

	/**
	 * @return the rejected
	 */
	public String getRejected() {
		return rejected;
	}

	public String getPend_internal() {
		return pend_internal;
	}

	public void setPend_internal(String pend_internal) {
		this.pend_internal = pend_internal;
	}

	/**
	 * @param rejected the rejected to set
	 */
	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	/**
	 * @return the passed
	 */
	public String getPassed() {
		return passed;
	}

	/**
	 * @param passed the passed to set
	 */
	public void setPassed(String passed) {
		this.passed = passed;
	}

}
