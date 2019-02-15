package com.android.incongress.cd.conference.beans;

import java.io.Serializable;
import java.util.List;

/**
 * posterCode No.1124
	conField 感染学组

	title 标题
	author 作者
	posterPicUrl 跳转图片
	maxCount 总共的页数
 	email 专家联系邮箱
 * @author Administrator
 *
 */
public class DZBBBean{

	/**
	 * array : [{"posterId":34509,"posterCode":"AS-0007","author":"韩战营","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"The Association of Serum Thyroid Hormones Levels with Slow Coronary Flow Phenomenon","url":"http://app.incongress.cn/files/LED/44/img/AS-0007.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0007.jpg","disCount":0},{"posterId":34424,"posterCode":"AS-0010","author":"Ru Liu","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"Comparing Effectiveness of 3 Strategies for Real-World Stable Coronary Artery Disease Patients with Three-Vessel Disease: 2-Year Outcome Based on A Large Single-center Sample","url":"http://app.incongress.cn/files/LED/44/img/AS-0010.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0010.jpg","disCount":0},{"posterId":34414,"posterCode":"AS-0011","author":"魏蓓蕾","fieldId":1432,"field":"6.Drug Therapy","posterTitle":"Safety and Efficacy of Different Antithrombosis Regimens in Patients with Acute Coronary Syndrome Treated with Elective Percutaneous Coronary Intervention in Perioperative Period","url":"http://app.incongress.cn/files/LED/44/img/AS-0011.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0011.jpg","disCount":0},{"posterId":34518,"posterCode":"AS-0012","author":"张明明","fieldId":1428,"field":"2.Structural Heart Disease","posterTitle":"Melatonin Protects Against Diabetic Cardiomyopathy Through Mst1/Sirt3 Signaling","url":"http://app.incongress.cn/files/LED/44/img/AS-0012.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0012.jpg","disCount":0},{"posterId":34393,"posterCode":"AS-0016","author":"熊恬园","fieldId":1428,"field":"2.Structural Heart Disease","posterTitle":"Importance of Prosthesis Oversizing in the Need for Permanent Pacemaker Implantation after Transcatheter Aortic Valve Replacement in Bicuspid Aortic Valve Patients","url":"http://app.incongress.cn/files/LED/44/img/AS-0016.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0016.jpg","disCount":0},{"posterId":34519,"posterCode":"AS-0019","author":"徐娜","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"Predictive Value of Neutrophil to Lymphocyte Ratio in Long-term Outcomes of Left Main and/or Three-vessel Disease in Patients with Acute Myocardial Infarction","url":"http://app.incongress.cn/files/LED/44/img/AS-0019.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0019.jpg","disCount":0},{"posterId":34425,"posterCode":"AS-0020","author":"张颖倩","fieldId":1431,"field":"5.Fundamental Research","posterTitle":"Delayed Reendothelialization with Rapamycin is Rescued by the Addition of Nicorandil in Balloon-injured Rat Carotid Arteries","url":"http://app.incongress.cn/files/LED/44/img/AS-0020.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0020.jpg","disCount":0},{"posterId":34394,"posterCode":"AS-0023","author":"张璐","fieldId":1430,"field":"4.Cardiac Arrhythmias","posterTitle":"The Sestrin2 Determination of Patients with Atrial Fibrillation and The Analysis of Relevant Factors","url":"http://app.incongress.cn/files/LED/44/img/AS-0023.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0023.jpg","disCount":0},{"posterId":34555,"posterCode":"AS-0024","author":"徐绍鹏","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"The Lower the Left Ventricular Ejection Fraction, the More Benefit from Successful Chronic Total Occlusion Intervention","url":"http://app.incongress.cn/files/LED/44/img/AS-0024.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0024.jpg","disCount":0},{"posterId":34461,"posterCode":"AS-0027","author":"吕续成","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"Healing Score of the Xinsorb Scaffold in the Treatment of De Novo Lesions: 6-Month Imaging Outcomes","url":"http://app.incongress.cn/files/LED/44/img/AS-0027.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0027.jpg","disCount":0},{"posterId":34556,"posterCode":"AS-0028","author":"王江友","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"Effect of Alprostadil on Preventing Contrast-Induced Nephropathy in Diabetic Patients with Rrenal Insufficiency","url":"http://app.incongress.cn/files/LED/44/img/AS-0028.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0028.jpg","disCount":0},{"posterId":34468,"posterCode":"AS-0029","author":"郭方明","fieldId":1427,"field":"1.Coronary Artery Disease","posterTitle":"The Relationship between Local MMP-9 and Infarct Related Artery Reflow in Acute STEMI Patients","url":"http://app.incongress.cn/files/LED/44/img/AS-0029.jpg","point":0,"readCount":0,"posterPicUrl":"http://app.incongress.cn/files/LED/44/img/AS-0029.jpg","disCount":0}]
	 * maxCount : 99
	 */

	private int maxCount;
	private List<ArrayBean> array;

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public List<ArrayBean> getArray() {
		return array;
	}

	public void setArray(List<ArrayBean> array) {
		this.array = array;
	}

	public static class ArrayBean implements Serializable{
		public ArrayBean(){
			super();
		}
		public ArrayBean(int posterId, String posterCode, String author, int fieldId, String field, String posterTitle, String url, String point, int readCount, String posterPicUrl, int disCount) {
			this.posterId = posterId;
			this.posterCode = posterCode;
			this.author = author;
			this.fieldId = fieldId;
			this.field = field;
			this.posterTitle = posterTitle;
			this.url = url;
			this.point = point;
			this.readCount = readCount;
			this.posterPicUrl = posterPicUrl;
			this.disCount = disCount;
		}

		/**
		 * posterId : 34509
		 * posterCode : AS-0007
		 * author : 韩战营
		 * fieldId : 1427
		 * field : 1.Coronary Artery Disease
		 * posterTitle : The Association of Serum Thyroid Hormones Levels with Slow Coronary Flow Phenomenon
		 * url : http://app.incongress.cn/files/LED/44/img/AS-0007.jpg
		 * point : 0
		 * readCount : 0
		 * posterPicUrl : http://app.incongress.cn/files/LED/44/img/AS-0007.jpg
		 * disCount : 0
		 */


		private int posterId;
		private String posterCode;
		private String author;
		private int fieldId;
		private String field;
		private String posterTitle;
		private String url;
		private String point;
		private int readCount;
		private String posterPicUrl;
		private int disCount;

		public int getPosterId() {
			return posterId;
		}

		public void setPosterId(int posterId) {
			this.posterId = posterId;
		}

		public String getPosterCode() {
			return posterCode;
		}

		public void setPosterCode(String posterCode) {
			this.posterCode = posterCode;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public int getFieldId() {
			return fieldId;
		}

		public void setFieldId(int fieldId) {
			this.fieldId = fieldId;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getPosterTitle() {
			return posterTitle;
		}

		public void setPosterTitle(String posterTitle) {
			this.posterTitle = posterTitle;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPoint() {
			return point;
		}

		public void setPoint(String point) {
			this.point = point;
		}

		public int getReadCount() {
			return readCount;
		}

		public void setReadCount(int readCount) {
			this.readCount = readCount;
		}

		public String getPosterPicUrl() {
			return posterPicUrl;
		}

		public void setPosterPicUrl(String posterPicUrl) {
			this.posterPicUrl = posterPicUrl;
		}

		public int getDisCount() {
			return disCount;
		}

		public void setDisCount(int disCount) {
			this.disCount = disCount;
		}
	}
}
