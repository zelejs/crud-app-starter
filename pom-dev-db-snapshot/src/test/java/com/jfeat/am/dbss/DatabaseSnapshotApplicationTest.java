package com.jfeat.am.dbss;

import com.jfeat.am.dbss.services.model.Message;
import com.jfeat.am.dbss.services.service.DatabaseSnapshotService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DatabaseSnapshotApplication.class)
public class DatabaseSnapshotApplicationTest {
	protected static final Logger logger = LoggerFactory.getLogger(DatabaseSnapshotApplicationTest.class);

	@Autowired
	private DatabaseSnapshotService snapshotService;

	@Test
	public void testConfig() throws IOException {
		Date date = new Date(new java.util.Date().getTime());
		String dateString = date.toString();


		"".toString();
	}


	//@Test
	public void testSnapshotBackup() throws Exception {
		String author = "test";

		try {
			List<Message> records = snapshotService.createSnapshotBy(author);
			records.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void testRestoreDatabase() throws Exception{
		String author = "test";
		String sql =  "1509870920351.sql";

		snapshotService.restoreSnapshotBy(author, sql);
	}


	//@Test
	public void testSnapshotRestore() throws Exception {
		String author = "test";
		String sql =  "1509870920351.sql";

		try {
			List<Message> records = snapshotService.restoreSnapshotBy(author, sql);
			records.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
