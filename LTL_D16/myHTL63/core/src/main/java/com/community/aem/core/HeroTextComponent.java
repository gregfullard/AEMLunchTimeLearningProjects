package com.community.aem.core;

import com.adobe.cq.sightly.WCMUsePojo;

public class HeroTextComponent extends WCMUsePojo {

  private HeroTextBean heroTextBean = null;
 
  @Override
  public void activate() throws Exception {

    System.out.println("Activating the Hero Text Component");
    
    heroTextBean = new HeroTextBean();
    String heading;
    String description;

    // Get the values that the author entered into the AEM dialog
    heading = getProperties().get("jcr:heading", "");
    description = getProperties().get("jcr:description", "");

    System.out.println("Heading retrieved from JCR:" +heading);
    
    heroTextBean.setHeadingText(heading);
    heroTextBean.setDescription(description); 
    
    System.out.println("Hero text bean:" +heroTextBean.toString());
  }

  public HeroTextBean getHeroTextBean() {
    return heroTextBean;
  }

  public void setHeroTextBean(HeroTextBean heroTextBean) {
    this.heroTextBean = heroTextBean;
  }

}
