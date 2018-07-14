package com.ljcr.avro;

import com.example.avro.EmailAddress;
import com.ljcr.api.Workspace;
import com.ljcr.tests.UserRepositorySupport;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * User
 * /
 * <p>
 * assertUser
 * /id=123(int)
 * /username=myuser(string)
 * /passwordHash=some(string)
 * /signupDate=123L (long) // timestamp
 * <p>
 * // assertEmails
 * /emailAddresses=array<EmailAddress>
 * /emailAddresses/1/address=some@one.com(string)
 * /emailAddresses/1/dateAdded=123456L (long)
 * /emailAddresses/2/verified=false (boolean)
 * /emailAddresses/2/address=some@two.com(string)
 * /emailAddresses/2/dateAdded=1234567890L (long)
 * /emailAddresses/2/dateBounced=null (long)
 * /emailAddresses/2/verified=true (boolean)
 */
public class AvroAdapterTest extends UserRepositorySupport {

    @Override
    public Workspace createWs() {
        try {
            com.example.avro.User user1 = createUser();
            serializeToFile(user1, "/tmp/test.avro");

            return AvroAdapter.createWs(new File("/tmp/test.avro"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void serializeToFile(com.example.avro.User user1, String fileName) throws IOException {
        DatumWriter<com.example.avro.User> userDatumWriter = new SpecificDatumWriter<com.example.avro.User>(com.example.avro.User.class);
        DataFileWriter<com.example.avro.User> dataFileWriter = new DataFileWriter<com.example.avro.User>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File(fileName));
        dataFileWriter.append(user1);
        dataFileWriter.close();
    }

    private com.example.avro.User createUser() {
        com.example.avro.User user1 = com.example.avro.User.newBuilder()
                .setId(10)
                .setUsername("myuser")
                .setPasswordHash("DEADBEAF")
                .setSignupDate(123L)
                .setEmailAddresses(Arrays.asList(
                        EmailAddress.newBuilder().setAddress("some@one.com")
                                .setDateAdded(123456L)
                                .setDateBounced(123456L)
                                .build(),
                        EmailAddress.newBuilder().setAddress("some@two.com")
                                .setDateAdded(1234567890L)
                                .setDateBounced(null)
                                .setVerified(true)
                                .build()))
                .setTwitterAccounts(Collections.emptyList())
                .setToDoItems(Collections.emptyList())
                .build();
        return user1;
    }
}