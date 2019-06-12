package com.data.integration.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.data.integration.service.enums.ApplicationVariableDatatypeEnum;
import com.data.integration.service.enums.ApplicationVariableTypeEnum;

@Entity
@Table(name = "ApplicationVariable")
public class ApplicationVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApplicationVariableID", unique = true, nullable = false)
    private Long applicationVariableID;

    @Column(name = "Keyword")
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type")
    private ApplicationVariableTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(name = "DataType")
    private ApplicationVariableDatatypeEnum dataType;

    @Column(name = "Value")
    private String value;

    @Column(name = "SubscriberID")
    private Long subscriberID;

    /**
	 * 
	 */
    public ApplicationVariable() {
        super();
    }

    /**
     * @param applicationVariableID
     * @param keyword
     * @param type
     * @param dataType
     * @param value
     * @param subscriberID
     */
    public ApplicationVariable(Long applicationVariableID, String keyword,
            ApplicationVariableTypeEnum type,
            ApplicationVariableDatatypeEnum dataType, String value,
            Long subscriberID) {
        super();
        this.applicationVariableID = applicationVariableID;
        this.keyword = keyword;
        this.type = type;
        this.dataType = dataType;
        this.value = value;
        this.subscriberID = subscriberID;
    }

    public Long getApplicationVariableID() {
        return applicationVariableID;
    }

    public void setApplicationVariableID(Long applicationVariableID) {
        this.applicationVariableID = applicationVariableID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ApplicationVariableTypeEnum getType() {
        return type;
    }

    public void setType(ApplicationVariableTypeEnum type) {
        this.type = type;
    }

    public ApplicationVariableDatatypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(ApplicationVariableDatatypeEnum dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(Long subscriberID) {
        this.subscriberID = subscriberID;
    }

}
