package br.com.telefonica.cobranca.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Document
public class CobrancaMongoDB {

	@Column(name = "billing_id")
	private Long billingId;
	@Column(name = "customer_id")
	private Long customerId;
	@Column(name = "billing_vencimento")
	private Long billingVencimento;
	@Column(name = "billing_status")
	private String billingStatus;
	@Column(name = "billing_data_pgto")
	private Long billingDataPgto;
	@Column(name = "billing_valor_fatura")
	private Double billingValorFatura;
	@Column(name = "__deleted")
	private Boolean deleted;
	@Column(name = "processed")
	private Boolean processed;

	public void setBilling_id(Long billing_id) {
		this.billingId = billing_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customerId = customer_id;
	}
	public void setBilling_vencimento(Long billing_vencimento) {
		this.billingVencimento = billing_vencimento;
	}
	public void setBilling_status(String billing_status) {
		this.billingStatus = billing_status;
	}
	public void setBilling_data_pgto(Long billing_data_pgto) {
		this.billingDataPgto = billing_data_pgto;
	}
	public void setBilling_valor_fatura(Double billing_valor_fatura) {
		this.billingValorFatura = billing_valor_fatura;
	}
	public void set__deleted(Boolean __deleted) {
		this.deleted = __deleted;
	}
	public Long getBilling_id() {
		return billingId;
	}
	public Long getCustomer_id() {
		return customerId;
	}
	public Long getBilling_vencimento() {
		return billingVencimento;
	}
	public String getBilling_status() {
		return billingStatus;
	}
	public Long getBilling_data_pgto() {
		return billingDataPgto;
	}
	public Double getBilling_valor_fatura() {
		return billingValorFatura;
	}
	public Boolean get__deleted() {
		return deleted;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

}
