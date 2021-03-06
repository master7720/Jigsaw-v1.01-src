package me.robbanrobbin.jigsaw.hackerdetect;

import java.util.ArrayList;

import me.robbanrobbin.jigsaw.client.modules.HackerDetect;
import me.robbanrobbin.jigsaw.client.modules.Ping;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Accuracy;
import me.robbanrobbin.jigsaw.hackerdetect.checks.AntiKBCheck;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Check;
import me.robbanrobbin.jigsaw.hackerdetect.checks.CheckState;
import me.robbanrobbin.jigsaw.hackerdetect.checks.HighAPS;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Yawrate;
import net.minecraft.entity.player.EntityPlayer;

public class Hacker {

	public EntityPlayer player;
	public ArrayList<Check> checks = new ArrayList<Check>();
	public int maxAps = 0;
	public double maxYawrate = 0;
	public float accuracyValue = 0.0f;
	public ArrayList<Float> accuracyValues = new ArrayList<Float>();
	boolean didIntercept = false;
	public boolean muted = false;
	public boolean loaded = true;

	public Hacker(EntityPlayer player) {
		this.player = player;
		checks.add(new AntiKBCheck());
		checks.add(new HighAPS());
		checks.add(new Yawrate());
		checks.add(new Accuracy());
	}

	public void updateEnabledChecks() {
		for (Check check : HackerDetect.checks) {
			Check found = HackerDetect.getCheck(this.player.getName(), check.getName());
			found.setEnabled(check.isEnabled());
		}
	}

	public void doChecks() {
		updateEnabledChecks();
		maxAps = Math.max(maxAps, player.aps);
		maxYawrate = Math.max(maxYawrate, Math.abs(player.rotationYaw - player.prevRotationYaw));
		for (Check check : this.checks) {
			if (!check.isEnabled()) {
				continue;
			}
			if (Ping.timer.getTime() < 200) {
				if (didIntercept) {
					didIntercept = false;
					return;
				}
				CheckState state = check.check(this);
				if (state == CheckState.VIOLATION) {
					check.timer.reset();
					check.tempViolations++;
				} else if(state == CheckState.RESET) {
					if (check.timer.hasTimeElapsed(check.getDecayTime(), false)) {
						check.tempViolations = 0;
					}
				}
				else if(state == CheckState.IDLE) {
					
				}

			} else {
				didIntercept = true;
			}
			if (check.tempViolations >= check.getMaxViolations()) {
				check.violate();
				if (!muted) {
//					if (check.getMentionName()) {
//						Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, 
//								"Player " + "" + player.getName() + check.getPrefix() + check.getName() + " vl=(" + check.getViolations() + ")"));
//					} else {
//						Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, 
//								"Player " + "" + player.getName() + check.getPrefix() + " vl=(" + check.getViolations() + ")"));
//					}
				}
			}
		}
	}

	public int getViolations() {
		int i = 0;
		for (Check check : checks) {
			if (!check.isEnabled()) {
				continue;
			}
			i += check.getViolations();
		}
		return i;
	}
	
	public boolean isAccuracyListUsable() {
		return accuracyValues.size() >= 10;
	}
	
	public void updateAccuracyList(float accuracy) {
		accuracyValues.add(accuracy);
		if(accuracyValues.size() > 200) {
			accuracyValues.remove(0);
		}
	}

}
