package com.ljcr.dynamics;

import com.example.avro.EmailAddress;
import com.example.avro.Identity;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.Repository;
import com.ljcr.tests.UserRepositorySupport;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

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
    public Repository createWs() {
        try {
            com.example.avro.User user1 = createUserRepository();
            serializeToFile(user1, "/tmp/test.avro");

            return AvroAdapter.createWs(new File("/tmp/test.avro"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testMap() {
        Repository ws = createWs();
        ImmutableNode identities = ws.getRootNode().getItem("identities");
        assertThat(identities, notNullValue());
        ImmutableNode fb = identities.getItem("facebook");
        assertThat(fb, notNullValue());
        assertThat(fb.getItem("secretId").getValue(), equalTo("some@one.com"));
        assertThat(identities.getElements()
                .map(i -> i.getName())
                .collect(toList()), hasItems("facebook", "google", "microsoft", "twitter"));
    }

    private void serializeToFile(com.example.avro.User user1, String fileName) throws IOException {
        DatumWriter<com.example.avro.User> userDatumWriter = new SpecificDatumWriter<com.example.avro.User>(com.example.avro.User.class);
        DataFileWriter<com.example.avro.User> dataFileWriter = new DataFileWriter<com.example.avro.User>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File(fileName));
        dataFileWriter.append(user1);
        dataFileWriter.close();
    }

    private com.example.avro.User createUserRepository() {
        Map<String, Identity> ids = new HashMap<>();
        ids.put("facebook", Identity.newBuilder().setSecretId("some@one.com").build());
        ids.put("google", Identity.newBuilder().setSecretId("some@gmail.com").build());
        ids.put("microsoft", Identity.newBuilder().setSecretId("some@microsoft.com").build());
        ids.put("twitter", Identity.newBuilder().setSecretId("some@two.com").build());

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
                .setIdentities(ids)
                .build();
        return user1;
    }
}