package com.simple.sjge.entities.extensions;

import com.simple.sjge.entities.Entity;

public class Shield {

	private int capacity = 100;
	private int current = 100;
	protected Entity owner;
	private int ticksEmpty = 0;
	boolean charging = false;
	private int chargeDelay = 20;
	private int chargeRate = 1;

	public Shield(Entity owner, int capacity) {
		this(owner, capacity, capacity, 20);
	}

	public Shield(Entity owner, int capacity, int current) {
		this(owner, capacity, current, 20);
	}
	
	public Shield(Entity owner, int capacity, int current, int chargeDelay) {
		this(owner, capacity, current, chargeDelay, 1);
	}

	public Shield(Entity owner, int capacity, int current, int chargeDelay, int chargeRate) {
		this.owner = owner;
		this.capacity = capacity;
		this.current = current;
		this.chargeDelay = chargeDelay;
		this.chargeRate = chargeRate;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getCurrent() {
		return this.current;
	}

	public Entity getOwner() {
		return this.owner;
	}

	public void setCurrent(int curr) {
		int a = curr;
		if (a < 0)
			this.current = 0;
		else if (a > this.capacity)
			this.current = this.capacity;
		else
			this.current = a;
		this.ticksEmpty = 0;
		this.charging = false;
	}

	public void tick() {
		if (this.current != this.capacity && ticksEmpty < chargeDelay && !charging)
			this.ticksEmpty++;
		if (ticksEmpty >= chargeDelay) {
			this.charging = true;
			ticksEmpty = 0;
		}
		if (charging) {
			this.current += chargeRate;
		}
		if (this.current >= this.capacity) {
			this.charging = false;
			this.current = this.capacity;
		}
	}

	public int getTicksEmpty() {
		return ticksEmpty;
	}

	public boolean isCharging() {
		return this.charging;
	}

	public void setCapacity(Integer valueOf) {
		this.capacity = valueOf;
		if (this.current > valueOf)
			this.current = valueOf;
	}
	
	public int damage(float dam) {
		return damage((int)dam);
	}

	public int damage(int dam) {
		int shield = this.current - dam;
		setCurrent(this.current - dam);
		if (shield < 0)
			return +shield;
		return 0;
	}

	private void setCurrent(float f) {
		setCurrent((int)f);
	}

	public boolean isCharged() {
		return this.current == this.capacity;
	}

	public boolean isEmpty() {
		return this.current == 0;
	}

	public void resetRecharge() {
		this.charging = false;
		ticksEmpty = 0;
	}

}
