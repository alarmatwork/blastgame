package com.fitness.blast.integrations

import com.fitness.blast.dao.Game
import com.fitness.blast.dao.Point
import com.fitness.blast.dao.User
import org.junit.Ignore
import spock.lang.Specification

@Ignore
class CopernicusApiServiceSpec extends Specification {

    CopernicusApiService service = new CopernicusApiService();

    def "URL replace"() {
        given:


        when:
            String url = service.getPointType(58.377625, 26.729006);

        then:
            url == "X"


    }
}
