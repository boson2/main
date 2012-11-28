package org.tok.cust.tool.model;

import java.io.Serializable;

public class ZipCodeVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//primary key;
	private String zipCode;	//우편번호, 필드길이 7
	private String seq;		//데이터 순서	5
	private String lank_knd;
	
	//value 총 7개, 
	private String sido;	//특별시, 광역시, 도	6, 서울,경기,전북,전남,충북,충남,경북,경남,제주 등으로 표시
	private String gugun;	//시,군,구	17
	private String dong;	//읍,면,동	26
	private String ri;		//리명	18
	private String bldg;	//건물명 40
	private String st_bunji;//시작번지	9
	private String ed_bunji;//끝번지	9
	
	private String address; //번지 포함
	
	private String simpleAddress; // 번지 제외
	/**
	 * 총 우편번호 데이터 수 51,360개
	 */
	
	public ZipCodeVo() {
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getLank_knd() {
		return lank_knd;
	}

	public void setLank_knd(String lank_knd) {
		this.lank_knd = lank_knd;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getRi() {
		return ri;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getBldg() {
		return bldg;
	}

	public void setBldg(String bldg) {
		this.bldg = bldg;
	}

	public String getSt_bunji() {
		return st_bunji;
	}

	public void setSt_bunji(String st_bunji) {
		this.st_bunji = st_bunji;
	}

	public String getEd_bunji() {
		return ed_bunji;
	}

	public void setEd_bunji(String ed_bunji) {
		this.ed_bunji = ed_bunji;
	}

	//번지 포함 주소 리턴
	public String getAddress() {
		if(ri == null){
			if(bldg == null){
				if(st_bunji == null){
					address = sido + " " + gugun + " " + dong;
				}
				else if(st_bunji != null){
					if(ed_bunji == null){
						address = sido + " " + gugun + " " + dong + " " + st_bunji;
					}
					else if(ed_bunji != null){
						address = sido + " " + gugun + " " + dong + " " + st_bunji + " ~ " + ed_bunji;
					}
				}
			}
			else if(bldg != null){
				if(st_bunji == null){
					address = sido + " " + gugun + " " + dong + " " + bldg;
				}
				else if(st_bunji != null){
					if(ed_bunji == null){
						address = sido + " " + gugun + " " + dong + " " + bldg + " " + st_bunji;
					}
					else if(ed_bunji != null){
						address = sido + " " + gugun + " " + dong + " " + bldg + " " + st_bunji + " ~ " + ed_bunji;
					}
				}
			}
		}
		else if(ri != null){
			if(bldg == null){
				if(st_bunji == null){
					address = sido + " " + gugun + " " + dong + " " + ri;
				}
				else if(st_bunji != null){
					if(ed_bunji == null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + st_bunji;
					}
					else if(ed_bunji != null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + st_bunji + " ~ " + ed_bunji;
					}
				}
			}
			else if(bldg != null){
				if(st_bunji == null){
					address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg;
				}
				else if(st_bunji != null){
					if(ed_bunji == null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg + " " + st_bunji;
					}
					else if(ed_bunji != null){
						address = sido + " " + gugun + " " + dong + " " + ri + " " + bldg + " " + st_bunji + " ~ " + ed_bunji;
					}
				}
			}
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getSimpleAddress() {
		if(ri == null){
			if(bldg == null){
				simpleAddress = sido + " " + gugun + " " + dong;
			}
			else if(bldg != null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + bldg;
			}
		}
		else if(ri != null){
			if(bldg == null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + ri;
			}
			else if(bldg != null){
				simpleAddress = sido + " " + gugun + " " + dong + " " + ri + " " + bldg;
			}
		}
		return simpleAddress;
	}

	public void setSimpleAddress(String simpleAddress) {
		this.simpleAddress = simpleAddress;
	}

	public String toString(){
		return getAddress();
	}
}
