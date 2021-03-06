/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.ken.services.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.service.NotificationMessageDeliveryAutoRemovalService;
import org.kuali.rice.ken.service.ProcessingResult;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.isNotNull;

/**
 * Tests NotificationMessageDeliveryAutoRemovalServiceImpl
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Ignore // deadlocks are detected during clear database lifecycle (even when select for update is commented out...)
public class NotificationMessageDeliveryAutoRemovalServiceImplTest extends KENTestCase {
	// NOTE: this value is highly dependent on test data 
	private static final int EXPECTED_SUCCESSES = 6;

	protected void assertProcessResults() {
		// one error should have occurred and the delivery should have been marked unlocked again
		Collection<NotificationMessageDelivery> lockedDeliveries = getLockedDeliveries();

		assertEquals(0, lockedDeliveries.size());

		// should be 1 autoremoved delivery
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(equal(NotificationConstants.BO_PROPERTY_NAMES.MESSAGE_DELIVERY_STATUS,
                NotificationConstants.MESSAGE_DELIVERY_STATUS.AUTO_REMOVED));
		Collection<NotificationMessageDelivery> unprocessedDeliveries =
                KRADServiceLocator.getDataObjectService().findMatching(NotificationMessageDelivery.class,
                criteria.build()).getResults();

        assertEquals(EXPECTED_SUCCESSES, unprocessedDeliveries.size());
	}

	/**
	 * Test auto-removal message deliveries
	 */
	@Test
	public void testAutoRemovedNotificationMessageDeliveries() {
		NotificationMessageDeliveryAutoRemovalService nSvc = services.getNotificationMessageDeliveryAutoRemovalService();

		services.getNotificationMessageDeliveryResolverService().resolveNotificationMessageDeliveries();

		ProcessingResult result = nSvc.processAutoRemovalOfDeliveredNotificationMessageDeliveries();

		assertEquals(0, result.getFailures().size());
		assertEquals(EXPECTED_SUCCESSES, result.getSuccesses().size());

		assertProcessResults();
	}

	/**
	 * Test concurrent auto-removal of message deliveries
	 */
	@Test
	public void testAutoRemovalConcurrency() throws InterruptedException {
		final NotificationMessageDeliveryAutoRemovalService nSvc = services.getNotificationMessageDeliveryAutoRemovalService();

		services.getNotificationMessageDeliveryResolverService().resolveNotificationMessageDeliveries();

		final ProcessingResult[] results = new ProcessingResult[2];
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				results[0] = nSvc.processAutoRemovalOfDeliveredNotificationMessageDeliveries();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				results[1] = nSvc.processAutoRemovalOfDeliveredNotificationMessageDeliveries();
			}
		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		// assert that ONE of the autoremovers got all the items, and the other got NONE of the items
		LOG.info("Results of thread #1: " + results[0]);
		LOG.info("Results of thread #2: " + results[1]);
		assertTrue((results[0].getSuccesses().size() == EXPECTED_SUCCESSES && results[0].getFailures().size() == 0 &&
                results[1].getSuccesses().size() == 0 && results[1].getFailures().size() == 0) ||
				(results[1].getSuccesses().size() == EXPECTED_SUCCESSES && results[1].getFailures().size() == 0 &&
                results[0].getSuccesses().size() == 0 && results[0].getFailures().size() == 0));

		assertProcessResults();

    }

    /**
     * Gets all locked deliveries for the given entity.
     * @return null
     */
    protected Collection<NotificationMessageDelivery> getLockedDeliveries() {
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(isNotNull(NotificationConstants.BO_PROPERTY_NAMES.LOCKED_DATE));
        Collection<NotificationMessageDelivery> lockedDeliveries = KRADServiceLocator.getDataObjectService().findMatching(
                NotificationMessageDelivery.class, criteria.build()).getResults();

        return null;
    }
}
