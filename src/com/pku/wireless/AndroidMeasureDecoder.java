package com.pku.wireless;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import Config.BloodPressureAgent;

import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IOID_Type;
import es.libresoft.openhealth.android.aidl.types.ITYPE;
import es.libresoft.openhealth.android.aidl.types.measures.IDateMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasureArray;
import es.libresoft.openhealth.android.aidl.types.measures.IValueMeasure;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.Logging;

public class AndroidMeasureDecoder {
	private List<Object> measures;
	private List<Object> attributes;
	private Iterator<Object> ims;
	private Iterator<Object> iat;
	private AndroidBloodMeasure bloodPressureMeasure;
	private PulseMeasure pulseMeasure;
	static Collection mListeners;
	private MeasureReporter mr;
	
	private boolean bloodPressureFlag;
	private boolean pulseFlag;

	public void setMeasureReporter(MeasureReporter mr){
		this.mr = mr;
	}
	public void init(){
		measures = mr.getMeasures();
		ims = measures.iterator();
		attributes = mr.getAttributes();
		iat = attributes.iterator();
	}
	
	public static void setMeasureListener(MeasureListener mListener) {
		if (mListeners == null) {
			mListeners = new HashSet();
		}
		mListeners.add(mListener);
	}
	public static void removeMeasureListener(MeasureListener mListener) {
		if (mListeners == null)
			return;
		mListeners.remove(mListener);
	}
	
	public void receiveMeasures() {
		if (!attributes.isEmpty()) {
			Logging.debug("测量的属性为: ");
			while (iat.hasNext()) {
				IAttribute attrib = (IAttribute) iat.next();
				receiveAttribute(attrib);
			}		
			if(bloodPressureFlag){
				Iterator iter = mListeners.iterator();
				while (iter.hasNext()) {
					MeasureListener mListener = (MeasureListener) iter.next();
					mListener.getBloodMeasure(new MeasureEvent(this,bloodPressureMeasure));
				}
				bloodPressureFlag = false;
			}
			else if(pulseFlag){
				Iterator iter = mListeners.iterator();
				while (iter.hasNext()) {
					MeasureListener mListener = (MeasureListener) iter.next();
					mListener.getPulseMeasure(new MeasureEvent(this,pulseMeasure));
				}
				pulseFlag = false;
			}
		}
	}
	

	public void receiveAttribute(IAttribute attrib) {
		switch (attrib.getAttrId()) {
		case Nomenclature.MDC_ATTR_ID_TYPE:
			decodeType((ITYPE) attrib.getAttr());
			break;
		case Nomenclature.MDC_ATTR_UNIT_CODE:
			decodeUnitCode((IOID_Type) attrib.getAttr());
			break;
		case Nomenclature.MDC_ATTR_ID_HANDLE:
			break;
		case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST:
			break;
		default:
			break;
		}
	}

	public void setMeasures(IMeasure measure) {
		if (measure instanceof IMeasureArray) {
			switch (measure.getMeasureType()) {
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC:				
				Logging.debug("数组:" + (((IMeasureArray) measure).getList()));
				if(bloodPressureFlag){
					bloodPressureMeasure.setHPressure(((IMeasureArray) measure).getList());
					bloodPressureMeasure.setLPressure(((IMeasureArray) measure).getList());
					bloodPressureMeasure.setAPressure(((IMeasureArray) measure).getList());
				}				
				break;
			default:
				Logging.debug("默认数组类型:" + measure.getMeasureType());
				break;
			}
		}
		else if (measure instanceof IValueMeasure) {
			switch (measure.getMeasureType()) {
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC:
				Logging.debug("数值:" + (((IValueMeasure) measure).getFloatType()));
				if(pulseFlag)
					pulseMeasure.setPulse(((IValueMeasure) measure).getFloatType());
				break;
			default:
				Logging.debug("默认数值类型:" + measure.getMeasureType());
				break;
			}
		}
		else if (measure instanceof IDateMeasure) {
			switch (measure.getMeasureType()) {
			case Nomenclature.MDC_ATTR_TIME_STAMP_ABS:
				Logging.debug("时间:" + ((IDateMeasure) measure).getTimeStamp().toString());
				if(bloodPressureFlag)
					bloodPressureMeasure.setDate(((IDateMeasure) measure).getTimeStamp());
				else if(pulseFlag)
					pulseMeasure.setDate(((IDateMeasure) measure).getTimeStamp());
				break;
			default:
				Logging.debug("默认时间类型:" + measure.getMeasureType());
				break;
			}
		}
	}

	public void decodeUnitCode(IOID_Type unit) {
		switch (unit.getType()) {
		case BloodPressureAgent.MDC_DIM_MMHG:
			if(bloodPressureFlag)
				bloodPressureMeasure.setUint("mmHg");
			break;
		case BloodPressureAgent.MDC_DIM_BEAT_PER_MIN:
			if(pulseFlag)
				pulseMeasure.setUint("搏/分");
			break;
		default:
			Logging.debug("默认单位:" + unit.getType());
			break;
		}
	}

	public void decodeType(ITYPE type) {
		switch (type.getCode().getType()) {
			case BloodPressureAgent.MDC_PRESS_BLD_NONINV:
				System.out.println("Nonivasive blood pressure");
				bloodPressureMeasure = new AndroidBloodMeasure();
				bloodPressureFlag = true;
				while (ims.hasNext()) {
					IMeasure measure = (IMeasure) ims.next();
					setMeasures(measure);
				}
				break;
			case BloodPressureAgent.MDC_PULS_RATE_NON_INV:
				System.out.println("Pulse");
				pulseMeasure = new PulseMeasure();
				pulseFlag = true;
				while (ims.hasNext()) {
					IMeasure measure = (IMeasure) ims.next();
					setMeasures(measure);
				}
				break;
			default:
				Logging.debug("默认类型:" + type.getCode().getType());
				break;
		}
	}
}
