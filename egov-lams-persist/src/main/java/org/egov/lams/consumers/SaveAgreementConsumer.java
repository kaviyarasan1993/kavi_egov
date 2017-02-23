package org.egov.lams.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.dao.AgreementDao;
import org.egov.lams.model.Agreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveAgreementConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(SaveAgreementConsumer.class);
	
	@Autowired
	private AgreementDao agreementDao;
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics ="agreement-save-db")
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:"+ record.key() +":"+ "value:" +record.value());
	    if (record.topic().equals("agreement-save-db")) {
			ObjectMapper objectMapper=new ObjectMapper();
			try {
				LOGGER.info("SaveAgreementConsumer agreement-save-db AgreementDao:"+agreementDao);
				agreementDao.saveAgreement(objectMapper.readValue(record.value(),Agreement.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}