package com.baolinetworktechnology.shejiquan.domain;

public class ReadMessageBean {
	private numcalss result;
	public String message;
	public boolean success;
	public numcalss getResult() {
		return result;
	}
	public class numcalss{
		private int numMessage;
		private int numComment;
		private int numPostsGood;
		public int getNumPostsGood() {
			return numPostsGood;
		}

		public void setNumPostsGood(int numPostsGood) {
			this.numPostsGood = numPostsGood;
		}

		public int getNumMessage() {
			return numMessage;
		}

		public void setNumMessage(int numMessage) {
			this.numMessage = numMessage;
		}

		public int getNumComment() {
			return numComment;
		}

		public void setNumComment(int numComment) {
			this.numComment = numComment;
		}
	}
}
