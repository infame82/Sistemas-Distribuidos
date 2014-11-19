package com.uag.sd.weathermonitor.model.layer.physical.channel;

import java.io.Serializable;

public class RFChannel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7798469320963501286L;

	public enum RF_CHANNEL{CH_11(2.4000F),CH_12(2.4070F),CH_13(2.4140F),CH_14(2.4210F),CH_15(2.4280F),
		CH_16(2.4350F),CH_17(2.4420F),CH_18(2.4490F),CH_19(2.4F),CH_20(2.4560F),CH_21(2.4630F),CH_22(2.4700F),
		CH_23(2.4770F),CH_24(2.4835F);
		private float frequency;
		private RF_CHANNEL(float frequency) {
			this.frequency = frequency;
		}
		public float getFrecuency() {
			return frequency;
		}
	};
	
	private RF_CHANNEL channel;
	private int energy;
	
	public RFChannel(RF_CHANNEL channel) {
		this.channel = channel;
		energy = 0;
	}

	public RF_CHANNEL getChannel() {
		return channel;
	}

	public void setChannel(RF_CHANNEL channel) {
		this.channel = channel;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null || !(o instanceof RFChannel)) {
			return false;
		}
		RFChannel auxChannel = (RFChannel) o;
		return auxChannel.channel == this.channel;
	}
	
	@Override
	public int hashCode() {
		return channel.hashCode();
	}
}
