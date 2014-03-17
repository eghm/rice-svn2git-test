/**
 * Copyright 2005-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.samplu.admin.test;

import edu.samplu.common.AdminMenuBlanketAppITBase;
import edu.samplu.common.ITUtil;
import org.apache.commons.lang.RandomStringUtils;

/**
 * tests that user 'admin', on blanket approving a new County maintenance document, results in a final document
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LocationCountyBlanketAppIT extends AdminMenuBlanketAppITBase {
    
    @Override
    protected String getLinkLocator() {
        return "link=County";
    }

   @Override
   public String blanketApprove() throws Exception {
        String docId = waitForDocId();
        waitAndType("//input[@id='document.documentHeader.documentDescription']", "Validation Test County");
        String countryLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.country.CountryBo!!).(((code:document.newMaintainableObject.countryCode,))).((`document.newMaintainableObject.countryCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;" + getBaseUrlString()+ "/kr/lookup.do;::::).anchor4']";
        waitAndClick(countryLookUp);
        waitAndType("code", "US");
        waitAndClick("//input[@name='methodToCall.search' and @value='search']");
        waitAndClick("link=return value");
        waitAndType("//input[@id='document.newMaintainableObject.code']", RandomStringUtils.randomAlphabetic(2).toUpperCase());
        String stateLookUp = "//input[@name='methodToCall.performLookup.(!!org.kuali.rice.location.impl.state.StateBo!!).(((countryCode:document.newMaintainableObject.countryCode,code:document.newMaintainableObject.stateCode,))).((`document.newMaintainableObject.countryCode:countryCode,document.newMaintainableObject.stateCode:code,`)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).(::::;" + getBaseUrlString() + "/kr/lookup.do;::::).anchor4']";
        waitAndClick(stateLookUp);
        waitAndType("code", "IN");
        waitAndClick("//input[@name='methodToCall.search' and @value='search']");
        waitAndClick("link=return value");
        String countyName = "Validation Test County"+ITUtil.DTS;
        waitAndType("//input[@id='document.newMaintainableObject.name']", countyName);
        waitAndClick("//input[@id='document.newMaintainableObject.active']");
        return docId;  
    }
}
 