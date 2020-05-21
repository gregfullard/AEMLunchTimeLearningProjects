package com.community.aem.core;

public class HeroTextBean {

	private String headingText;
	private String description;
	
	public String getHeadingText() {
		return headingText;
	}
	public void setHeadingText(String headingText) {
		this.headingText = headingText;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
  @Override
  public String toString() {
    return "HeroTextBean [headingText=" + headingText + ", description=" + description + "]";
  }
	
	
}
