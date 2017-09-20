package com.spring.common.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class DigesterXml {

	public static void main(String[] args) {
		InputStream is = DigesterXml.class.getResourceAsStream("/config/BattleNpc.xml");

		Digester digester = new Digester();
		digester.setValidating(false);
		digester.push(new CommonConfig());

		digester.addObjectCreate("object/properties/property", CommonProperty.class);
		digester.addSetProperties("object/properties/property");
		digester.addBeanPropertySetter("object/properties/property/name");
		digester.addBeanPropertySetter("object/properties/property/type");
		digester.addBeanPropertySetter("object/properties/property/public", "pub");
		digester.addBeanPropertySetter("object/properties/property/private", "pri");
		digester.addBeanPropertySetter("object/properties/property/save");
		digester.addBeanPropertySetter("object/properties/property/desc");

		digester.addObjectCreate("object/records/record", CommonRecord.class);
		digester.addSetProperties("object/records/record");
		digester.addBeanPropertySetter("object/records/record/name");
		digester.addBeanPropertySetter("object/records/record/cols");
		digester.addBeanPropertySetter("object/records/record/maxrows");
		digester.addBeanPropertySetter("object/records/record/save");
		digester.addBeanPropertySetter("object/records/record/visible");
		digester.addBeanPropertySetter("object/records/record/desc");

		digester.addObjectCreate("object/records/record/column", CommonColumn.class);
		digester.addSetProperties("object/records/record/column");
		digester.addBeanPropertySetter("object/records/record/column/type");
		digester.addBeanPropertySetter("object/records/record/column/name");

		digester.addSetNext("object/properties/property", "addCommonProperty");
		digester.addSetNext("object/records/record", "addCommonRecord");
		digester.addSetNext("object/records/record/column", "addCommonColumn");

		try {
			CommonConfig commonConfig = (CommonConfig) digester.parse(is);

			List<CommonProperty> propList = commonConfig.getPropList();

			for (CommonProperty commonProperty : propList) {
				System.out.println("commonProperty:" + commonProperty.getName() + "," + commonProperty.getPri() + "," + commonProperty.getDesc());
			}

			List<CommonRecord> recordList = commonConfig.getRecordList();

			for (CommonRecord commonRecord : recordList) {
				System.out.println("commonRecord:" + commonRecord.getName() + "," + commonRecord.getMaxrows());

				List<CommonColumn> list = commonRecord.getList();

				for (CommonColumn commonColumn : list) {
					System.out.println("commonColumn:" + commonColumn.getName() + "," + commonColumn.getType());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
