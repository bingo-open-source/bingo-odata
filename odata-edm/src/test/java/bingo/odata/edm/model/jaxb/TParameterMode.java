//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.30 at 07:44:23 ���� GMT+08:00 
//


package bingo.odata.edm.model.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TParameterMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TParameterMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="In"/>
 *     &lt;enumeration value="Out"/>
 *     &lt;enumeration value="InOut"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TParameterMode")
@XmlEnum
public enum TParameterMode {

    @XmlEnumValue("In")
    IN("In"),
    @XmlEnumValue("Out")
    OUT("Out"),
    @XmlEnumValue("InOut")
    IN_OUT("InOut");
    private final String value;

    TParameterMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TParameterMode fromValue(String v) {
        for (TParameterMode c: TParameterMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}