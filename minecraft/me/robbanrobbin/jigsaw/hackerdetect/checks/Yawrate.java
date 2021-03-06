package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.hackerdetect.Hacker;

public class Yawrate extends Check {

	@Override
	public CheckState check(Hacker en) {
		if (Math.abs(en.player.rotationYaw - en.player.prevRotationYaw) > 50 && en.player.swingProgress != 0
				&& en.player.aps >= 3) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "KillAura (Rotates too fast)";
	}

	@Override
	public int getMaxViolations() {
		return 10;
	}

	@Override
	public int getDecayTime() {
		return 1000;
	}
}
