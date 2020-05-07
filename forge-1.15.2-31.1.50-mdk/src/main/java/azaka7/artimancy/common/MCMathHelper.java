package azaka7.artimancy.common;

public class MCMathHelper {
	
	public static final int getXPFromLevelAndProgress(int level, float frac) {
		return getXPFromLevel(level) + getXPProgress(level, frac);
	}
	
	public static final int getXPFromLevel(int level) {
		if(level > 31) {
			return (int) ((4.5*Math.pow(level, 2)) - (162*level) +2220);
		} else if(level > 16) {
			return (int) ((2.5*Math.pow(level, 2))-(40.5*level)+360);
		}
		return (int) (Math.pow(level, 2) + (6*level));
	}
	
	public static final int getXPRequired(int level) {
		if(level > 30) {
			return (9*level)-158;
		} else if(level > 15) {
			return (5*level)-38;
		}
		return (2*level)+7;
	}
	
	public static final int getXPProgress(int level, float frac) {
		return (int) Math.round(frac * (getXPFromLevel(level+1)-getXPFromLevel(level)));
	}
	
}
