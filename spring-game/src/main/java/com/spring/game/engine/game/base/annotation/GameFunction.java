package com.snail.webgame.engine.game.base.annotation;

@FunctionalInterface
public interface GameFunction<K, T, R> {

	R apply(K k, T[] t);
}
