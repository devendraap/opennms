/*******************************************************************************
 * This file is part of OpenNMS(R).
 * <p>
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 * <p>
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 * <p>
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * <p>
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 * http://www.gnu.org/licenses/
 * <p>
 * For more information contact:
 * OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/
 * http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.bsm.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.spring.BeanUtils;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.netmgt.bsm.persistence.api.BusinessService;
import org.opennms.netmgt.bsm.persistence.api.BusinessServiceDao;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.api.AlarmDao;
import org.opennms.netmgt.dao.api.DistPollerDao;
import org.opennms.netmgt.dao.api.MonitoredServiceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml",
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath*:/META-INF/opennms/component-service.xml",
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
@Transactional
public class BusinessServiceManagerImplIT {

    @Autowired
    private BusinessServiceManager businessServiceManager;

    @Autowired
    private BusinessServiceDao businessServiceDao;

    @Autowired
    private MonitoredServiceDao monitoredServiceDao;

    @Autowired
    private DistPollerDao distPollerDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private AlarmDao alarmDao;

    @Autowired
    private DatabasePopulator populator;

    @Before
    public void before() {
        BeanUtils.assertAutowiring(this);
        populator.populateDatabase();
    }

    @After
    public void after() {
        populator.resetDatabase();
        for (BusinessService eachService : businessServiceDao.findAll()) {
            businessServiceDao.delete(eachService);
        }
    }

    @Test
    public void testCalculateStatus() {
        BusinessService service = createService("Dummy Business Service");
        BusinessService service2 = createService("Another Dummy Business Service");
        Long serviceId1 = businessServiceDao.save(service);
        Long serviceId2 = businessServiceDao.save(service2);

        // no ip services attached
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId1));
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId2));

        // ip services attached
        businessServiceManager.assignIpInterface(serviceId1, 5);
        businessServiceManager.assignIpInterface(serviceId1, 6);

        // should not have any effect
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId1));
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId2));


        // atach NORMAL alarm to service 1
        alarmDao.save(createAlarm(monitoredServiceDao.get(5), OnmsSeverity.NORMAL));
        Assert.assertEquals(OnmsSeverity.NORMAL, businessServiceManager.calculateStatus(serviceId1));
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId2));

        // atach CRITICAL alarm to service 1
        alarmDao.save(createAlarm(monitoredServiceDao.get(5), OnmsSeverity.CRITICAL));
        Assert.assertEquals(OnmsSeverity.CRITICAL, businessServiceManager.calculateStatus(serviceId1));
        Assert.assertEquals(OnmsSeverity.CLEARED, businessServiceManager.calculateStatus(serviceId2));
    }

    private BusinessService createService(String serviceName) {
        BusinessService service = new BusinessService();
        service.setName(serviceName);
        return service;
    }

    private OnmsAlarm createAlarm(OnmsMonitoredService monitoredService, OnmsSeverity severity) {
        OnmsAlarm alarm = new OnmsAlarm();
        alarm.setUei("dummy");
        alarm.setCounter(1);
        alarm.setDistPoller(distPollerDao.whoami());
        alarm.setNode(nodeDao.get(monitoredService.getNodeId()));
        alarm.setServiceType(monitoredService.getServiceType());
        alarm.setIpAddr(monitoredService.getIpAddress());
        alarm.setSeverity(severity);
        return alarm;
    }

}
