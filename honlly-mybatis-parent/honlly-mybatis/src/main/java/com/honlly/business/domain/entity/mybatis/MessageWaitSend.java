package com.honlly.business.domain.entity.mybatis;

import java.math.BigDecimal;
import java.util.Date;

import com.honlly.mybatis.Entity;
import com.honlly.mybatis.IdCol;
import com.honlly.mybatis.annotation.Column;
import com.honlly.mybatis.annotation.Sequence;
import com.honlly.mybatis.annotation.Table;

/**
 * 
 *
 * @created：2016-03-16
 * @author wangk
 */
@Table("CUST_MESSAGE_WAIT_SEND")
//@Sequence("SEQ_CUST_MESSAGE_WAIT_SEND")
public class MessageWaitSend extends Entity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 实体元数据(列属性常量接口)
     */
    public interface MessageWaitSendMeta extends IdCol {
        String totalPhoneNumber = "totalPhoneNumber";
        String source = "source";
        String content = "content";
        String acceptTime = "acceptTime";
        String remark = "remark";
        String checked = "checked";
        String sendNum = "sendNum";
        String status = "status";
        String mobilePhones = "mobilePhones";
    }

    /**
     * 列属性
     */
    /** 号码个数 */
    @Column
    private BigDecimal totalPhoneNumber;
    /** 发送方 */
    @Column
    private String source;
    /** 短信内容 */
    @Column
    private String content;
    /** 请求时间点 */
    @Column
    private Date acceptTime;
    /** 备注 */
    @Column
    private String remark;
    /** 是否检查 */
    @Column
    private BigDecimal checked;
    /** 补发次数 */
    @Column
    private BigDecimal sendNum;
    /** 处理状态 */
    @Column
    private BigDecimal status;
    /** 短信号码组 */
    @Column
    private String mobilePhones;
	public BigDecimal getTotalPhoneNumber() {
		return totalPhoneNumber;
	}
	public void setTotalPhoneNumber(BigDecimal totalPhoneNumber) {
		this.totalPhoneNumber = totalPhoneNumber;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getChecked() {
		return checked;
	}
	public void setChecked(BigDecimal checked) {
		this.checked = checked;
	}
	public BigDecimal getSendNum() {
		return sendNum;
	}
	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}
	public BigDecimal getStatus() {
		return status;
	}
	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	public String getMobilePhones() {
		return mobilePhones;
	}
	public void setMobilePhones(String mobilePhones) {
		this.mobilePhones = mobilePhones;
	}

}
