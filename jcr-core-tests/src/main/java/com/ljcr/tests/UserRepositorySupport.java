package com.ljcr.tests;

import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.Repository;
import com.ljcr.api.definitions.StandardTypes;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Base class to test sample UserRepository, which is structured in the following way
 * <p>
 * User
 * /
 * <p> - assertUser
 * /id=123(int)
 * /username=myuser(string)
 * /passwordHash=some(string)
 * /signupDate=123L (long) // timestamp
 * <p> - assertEmails
 * /emailAddresses=array<EmailAddress>
 * /emailAddresses/1/address=some@one.com(string)
 * /emailAddresses/1/dateAdded=123456L (long)
 * /emailAddresses/2/verified=false (boolean)
 * /emailAddresses/2/address=some@two.com(string)
 * /emailAddresses/2/dateAdded=1234567890L (long)
 * /emailAddresses/2/dateBounced=null (long)
 * /emailAddresses/2/verified=true (boolean)
 */
public abstract class UserRepositorySupport {

    /**
     * @return return custom implementations of a workspace
     */
    public abstract Repository createWs();

    @Test
    public void testUserValues() {
        Repository ws = createWs();

        assertThat(ws.getName(), equalTo("User"));

        ImmutableNode usr = ws.getRootNode();
        assertThat(usr.getName(), equalTo(""));
        assertThat(usr.isObject(), equalTo(Boolean.TRUE));
        assertThat(usr.isCollection(), equalTo(Boolean.FALSE));

        assertThat(usr.getItem("id").asLong(), equalTo(10L));
        assertThat(usr.getItem("username").getValue(), equalTo("myuser"));
        assertThat(usr.getItem("passwordHash").getValue(), equalTo("DEADBEAF"));
        assertThat(usr.getItem("signupDate").getValue(), equalTo(123L));
        assertThat(usr.getItem("emailAddresses").getValue(), notNullValue());
    }

    @Test
    public void testPaths() {
        Repository ws = createWs();

        assertThat(ws.getName(), equalTo("User"));

        ImmutableNode usr = ws.getRootNode();
        assertThat(usr.getName(), equalTo(""));

        assertThat(usr.getItem("id").getName(), equalTo("id"));
        assertThat(usr.getItem("id").getValue(), equalTo(ws.getItem(Paths.get("/id")).getValue()));

        ImmutableNode emailAddresses = usr.getItem("emailAddresses");
        assertThat(emailAddresses.getValue(), notNullValue());
        assertThat(emailAddresses.getName(), equalTo("emailAddresses"));

        assertThat(emailAddresses.getItem("1").getName().toString(), equalTo("1"));
        assertThat(emailAddresses.getItem("1").getName(), equalTo(ws.getItem(Paths.get("/emailAddresses/1")).getName()));
    }

    @Test
    public void testUserValueTypes() {
        Repository ws = createWs();

        assertThat(ws.getName(), equalTo("User"));

        ImmutableNode usr = ws.getRootNode();
        assertThat(usr.getName(), equalTo(""));

        assertThat(usr.getItem("id").getTypeDefinition(), equalTo(StandardTypes.LONG));
        assertThat(usr.getItem("username").getTypeDefinition(), equalTo(StandardTypes.STRING));
        assertThat(usr.getItem("passwordHash").getTypeDefinition(), equalTo(StandardTypes.STRING));
        assertThat(usr.getItem("signupDate").getTypeDefinition(), equalTo(StandardTypes.LONG));
        assertThat(usr.getItem("emailAddresses").isCollection(), equalTo(Boolean.TRUE));
    }

    interface X<T> {
        Stream<? super T> getItems();
    }

    private X getX() {
        return null;
    }

    @Test
    public void testEmailValues2() {
        X x = getX();
        Stream items = x.getItems();
        Object collect = items.collect(Collectors.toList());

        Stream y = Stream.of("X");
        Object l = y.collect(toList());

    }


    @Test
    public void testEmailValues() {
        Repository ws = createWs();

        assertThat(ws.getName(), equalTo("User"));

        ImmutableNodeCollection emails = ws.getRootNode().getItem("emailAddresses").asArrayNode();
        List<ImmutableNode> items = emails.getElements().collect(toList());
        assertThat(items.size(), equalTo(2));

        ImmutableNode item1 = emails.getItem("1");
        assertThat(item1.getItem("verified").getValue(), equalTo(Boolean.FALSE));

        ImmutableNode item2 = emails.getItem("2");
        assertThat(item2.getItem("address").getValue(), equalTo("some@two.com"));
        assertThat(item2.getItem("dateAdded").getValue(), equalTo(1234567890L));
        assertThat(item2.getItem("verified").getValue(), equalTo(Boolean.TRUE));
        assertThat(item2.getItem("dateBounced"), notNullValue());
        assertThat(item2.getItem("dateBounced").getValue(), nullValue());
    }

    @Test
    public void testEmailTypes() {
        Repository ws = createWs();

        assertThat(ws.getName(), equalTo("User"));

        ImmutableNode rootNode = ws.getRootNode();
        ImmutableNodeCollection emails = rootNode.getItem("emailAddresses").asArrayNode();
        assertThat(emails.getElements().collect(toList()).size(), equalTo(2));

        ImmutableNode item2 = emails.getItem("2");
        assertThat(item2.getItem("address").getTypeDefinition(), equalTo(StandardTypes.STRING));
        assertThat(item2.getItem("dateAdded").getTypeDefinition(), equalTo(StandardTypes.LONG));
        assertThat(item2.getItem("verified").getTypeDefinition(), equalTo(StandardTypes.BOOLEAN));
        assertThat(item2.getItem("dateBounced").getTypeDefinition(), equalTo(StandardTypes.ANYTYPE));
    }
}