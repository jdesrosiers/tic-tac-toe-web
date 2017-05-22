package tictactoeweb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class ArgumentsTest {

    @DataProvider
    public static Object[][] dataProviderValidArguments() {
        return new Object[][] {
            { new String[] {}, null },
            { new String[] { "-p", "5000" }, 5000 },
            { new String[] { "-p", "1234" }, 1234 }
        };
    }

    @Test
    @UseDataProvider("dataProviderValidArguments")
    public void getPortShouldReturnThePortFromTheArguments(String[] args, Integer port) {
        assertThat(Arguments.getPort(args), equalTo(Option.of(port)));
    }

    @DataProvider
    public static Object[][] dataProviderUnknownArguments() {
        return new Object[][] {
            { new String[] { "-f", "1234" } },
            { new String[] { "1234" } },
            { new String[] { "-f", "1234", "-p", "1234" } },
            { new String[] { "1234", "-p", "1234" } }
        };
    }

    @Test(expected=InvalidArgumentException.class)
    @UseDataProvider("dataProviderUnknownArguments")
    public void getPortShouldThrowGivenUnknownArguments(String[] args) {
        Arguments.getPort(args);
    }

    @DataProvider
    public static Object[][] dataProviderInvalidPortArguments() {
        return new Object[][] {
            { new String[] { "-p" } },
            { new String[] { "-p", "foo" } }
        };
    }

    @Test(expected=InvalidArgumentException.class)
    @UseDataProvider("dataProviderInvalidPortArguments")
    public void getPortShouldThrowGivenInvalidPortArguments(String[] args) {
        Arguments.getPort(args);
    }

}
