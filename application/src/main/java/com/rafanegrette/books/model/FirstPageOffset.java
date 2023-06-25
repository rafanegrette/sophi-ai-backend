package com.rafanegrette.books.model;

public enum FirstPageOffset {
	ONE(1),
	TWO(2);
	
	private Integer offset;
	private FirstPageOffset(Integer firstPageOffset) {
		this.offset = firstPageOffset;
	}
	
	public Integer getValue() {
		return offset;
	}
}
