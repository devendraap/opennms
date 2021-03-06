/*******************************************************************************
 * This file is part of OpenNMS(R).
 * 
 * Copyright (C) 2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 * 
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 * 
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *     http://www.gnu.org/licenses/
 * 
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.config.translator;


import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.core.xml.ValidateUsing;
import org.opennms.netmgt.config.utils.ConfigUtils;

/**
 * An element representing an assignment to an attribute of the event
 */
@XmlRootElement(name = "assignment")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("translator-configuration.xsd")
public class Assignment implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlAttribute(name = "type", required = true)
    private String m_type;

    @XmlAttribute(name = "name", required = true)
    private String m_name;

    @XmlAttribute(name = "default", required = false)
    private String m_default;

    /**
     * An element representing a value to be used in a
     *  translation. 
     */
    @XmlElement(name = "value", required = true)
    private Value m_value;

    public Assignment() {
    }

    public String getType() {
        return m_type;
    }

    public void setType(final String type) {
        m_type = ConfigUtils.assertNotEmpty(type, "type");
    }

    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        m_name = ConfigUtils.assertNotEmpty(name, "name");
    }

    public Value getValue() {
        return m_value;
    }

    public void setValue(final Value value) {
        m_value = ConfigUtils.assertNotNull(value, "value");
    }

    public String getDefault() {
        return m_default;
    }

    public void setDefault(final String defaultValue) {
        this.m_default = ConfigUtils.assertNotNull(defaultValue, "default");
    }

    public boolean hasDefault() {
        return m_default != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_type, m_name, m_default, m_value);
    }

    @Override
    public boolean equals(final Object obj) {
        if ( this == obj ) {
            return true;
        }

        if (obj instanceof Assignment) {
            final Assignment that = (Assignment)obj;
            return Objects.equals(this.m_type, that.m_type)
                    && Objects.equals(this.m_name, that.m_name)
                    && Objects.equals(this.m_default, that.m_default)
                    && Objects.equals(this.m_value, that.m_value);
        }
        return false;
    }

}
