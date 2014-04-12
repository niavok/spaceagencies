package com.spaceagencies.common.game;

import java.io.Serializable;


public class GameEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private final long mId;
    private final Game mGame;
	
	public GameEntity(Game world, long id) {
        this.mGame = world;
        this.mId = id;
	}
	
	public long getId() {
        return mId;
    }
	
	public Game getWorld() {
        return mGame;
    }
}
