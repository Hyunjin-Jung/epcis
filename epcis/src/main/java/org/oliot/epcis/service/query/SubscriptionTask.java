package org.oliot.epcis.service.query;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Level;
import org.oliot.epcis.configuration.ConfigurationServlet;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * Copyright (C) 2014 KAIST RESL
 *
 * This file is part of Oliot (oliot.org).
 *
 * @author Jack Jaewook Byun, Ph.D student Korea Advanced Institute of Science
 *         and Technology Real-time Embedded System Laboratory(RESL)
 *         bjw0829@kaist.ac.kr
 */
public class SubscriptionTask implements Job {

	/**
	 * Whenever execute method invoked according to the cron expression Query
	 * the database and send the result to the destination.
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String queryName = map.getString("queryName");
		// String subscriptionID = map.getString("subscriptionID");
		String dest = map.getString("dest");
		// String cronExpression = map.getString("cronExpression");
		String eventType = map.getString("eventType");
		String GE_eventTime = map.getString("GE_eventTime");
		String LT_eventTime = map.getString("LT_eventTime");
		String GE_recordTime = map.getString("GE_recordTime");
		String LT_recordTime = map.getString("LT_recordTime");
		String EQ_action = map.getString("EQ_action");
		String EQ_bizStep = map.getString("EQ_bizStep");
		String EQ_disposition = map.getString("EQ_disposition");
		String EQ_readPoint = map.getString("EQ_readPoint");
		String WD_readPoint = map.getString("WD_readPoint");
		String EQ_bizLocation = map.getString("EQ_bizLocation");
		String WD_bizLocation = map.getString("WD_bizLocation");
		String EQ_bizTransaction_type = map.getString("EQ_bizTransaction_type");
		String EQ_source_type = map.getString("EQ_source_type");
		String EQ_destination_type = map.getString("EQ_destination_type");
		String EQ_transformationID = map.getString("EQ_transformationID");
		String MATCH_epc = map.getString("MATCH_epc");
		String MATCH_parentID = map.getString("MATCH_parentID");
		String MATCH_inputEPC = map.getString("MATCH_inputEPC");
		String MATCH_outputEPC = map.getString("MATCH_outputEPC");
		String MATCH_anyEPC = map.getString("MATCH_anyEPC");
		String MATCH_epcClass = map.getString("MATCH_epcClass");
		String MATCH_inputEPCClass = map.getString("MATCH_inputEPCClass");
		String MATCH_outputEPCClass = map.getString("MATCH_outputEPCClass");
		String MATCH_anyEPCClass = map.getString("MATCH_anyEPCClass");
		String EQ_quantity = map.getString("EQ_quantity");
		String GT_quantity = map.getString("GT_quantity");
		String GE_quantity = map.getString("GE_quantity");
		String LT_quantity = map.getString("LT_quantity");
		String LE_quantity = map.getString("LE_quantity");
		String EQ_fieldname = map.getString("EQ_fieldname");
		String GT_fieldname = map.getString("GT_fieldname");
		String GE_fieldname = map.getString("GE_fieldname");
		String LT_fieldname = map.getString("LT_fieldname");
		String LE_fieldname = map.getString("LE_fieldname");
		String EQ_ILMD_fieldname = map.getString("EQ_ILMD_fieldname");
		String GT_ILMD_fieldname = map.getString("GT_ILMD_fieldname");
		String GE_ILMD_fieldname = map.getString("GE_ILMD_fieldname");
		String LT_ILMD_fieldname = map.getString("LT_ILMD_fieldname");
		String LE_ILMD_fieldname = map.getString("LE_ILMD_fieldname");
		String EXIST_fieldname = map.getString("EXIST_fieldname");
		String EXIST_ILMD_fieldname = map.getString("EXIST_ILMD_fieldname");
		String HASATTR_fieldname = map.getString("HASATTR_fieldname");
		String EQATTR_fieldname_attrname = map
				.getString("EQATTR_fieldname_attrname");
		String orderBy = map.getString("orderBy");
		String orderDirection = map.getString("orderDirection");
		String eventCountLimit = map.getString(" eventCountLimit");
		String maxEventCount = map.getString("maxEventCount");

		QueryService queryService = new QueryService();
		String pollResult = queryService.poll(queryName, eventType,
				GE_eventTime, LT_eventTime, GE_recordTime, LT_recordTime,
				EQ_action, EQ_bizStep, EQ_disposition, EQ_readPoint,
				WD_readPoint, EQ_bizLocation, WD_bizLocation,
				EQ_bizTransaction_type, EQ_source_type, EQ_destination_type,
				EQ_transformationID, MATCH_epc, MATCH_parentID, MATCH_inputEPC,
				MATCH_outputEPC, MATCH_anyEPC, MATCH_epcClass,
				MATCH_inputEPCClass, MATCH_outputEPCClass, MATCH_anyEPCClass,
				EQ_quantity, GT_quantity, GE_quantity, LT_quantity,
				LE_quantity, EQ_fieldname, GT_fieldname, GE_fieldname,
				LT_fieldname, LE_fieldname, EQ_ILMD_fieldname,
				GT_ILMD_fieldname, GE_ILMD_fieldname, LT_ILMD_fieldname,
				LE_ILMD_fieldname, EXIST_fieldname, EXIST_ILMD_fieldname,
				HASATTR_fieldname, EQATTR_fieldname_attrname, orderBy,
				orderDirection, eventCountLimit, maxEventCount);

		try {
 			URL url = new URL(dest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Length", "" + 
		               Integer.toString(pollResult.getBytes().length));
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(pollResult);
			wr.flush();
			wr.close();
			int x = conn.getResponseCode();
			System.out.println(x);
			conn.disconnect();
		} catch (MalformedURLException e) {
			ConfigurationServlet.logger.log(Level.ERROR, e.toString());
		} catch (IOException e) {
			ConfigurationServlet.logger.log(Level.ERROR, e.toString());
		}

	}
}