package com.fuzion.hg.manager.postion;

import lombok.Getter;

@Getter
public class PositionInformation {
	
	private String name;
	private int a;
	private int position;
	
	public PositionInformation(String name, int a, int position) {
		this.name = name;
		this.a = a;
		this.position = position;
	}

}
