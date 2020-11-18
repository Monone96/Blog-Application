package it.course.myblogc3.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="purchased_post")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PurchasedPost {

	@EmbeddedId
	private PurchasedPostId purchasedPostId;
	
	@Column(name="PURCHASE_DATE", 
		updatable=false, insertable=false, 
		columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date purchaseDate;

	public PurchasedPost(PurchasedPostId purchasedPostId) {
		super();
		this.purchasedPostId = purchasedPostId;
	}
	
	
	
}
