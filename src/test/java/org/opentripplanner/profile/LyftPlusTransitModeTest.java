package org.opentripplanner.profile;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentripplanner.api.parameter.QualifiedModeSet;
import org.opentripplanner.routing.core.TraverseModeSet;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.impl.DefaultStreetVertexIndexFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.opentripplanner.graph_builder.module.FakeGraph.*;

public class LyftPlusTransitModeTest {
    private static final Logger logger = LoggerFactory.getLogger(ConvertToFrequencyTest.class);

    private static Graph graph;

    @BeforeClass
    public static void beforeClass() throws Exception {
        graph = buildSFBayAreaGraph();
        addCaltrainTransit(graph);
        link(graph);
        graph.index(new DefaultStreetVertexIndexFactory());
    }

    @Test
    public void testWalkingSimpleRoute() throws Exception {
        ProfileRequest pr1 = new ProfileRequest();
        pr1.date = new LocalDate(2015, 6, 10);
        pr1.fromTime = 7 * 3600;
        pr1.toTime = 9 * 3600;
        pr1.fromLat = 37.778295;
        pr1.fromLon = -122.396579;
        pr1.toLat = 37.392243;
        pr1.toLon = -122.078776;
        pr1.accessModes = pr1.egressModes = pr1.directModes = new QualifiedModeSet("WALK");
        pr1.transitModes = new TraverseModeSet("TRANSIT");

        ProfileRouter router = new ProfileRouter(graph, pr1);
        ProfileResponse response = router.route();
        router.cleanup(); // destroy routing contexts even when an exception happens

        logger.info("{}", response);
    }

    @Test
    public void testLyftSimpleRoute() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.date = new LocalDate(2015, 6, 10);
        profileRequest.fromTime = 7 * 3600;
        profileRequest.toTime = 9 * 3600;
        profileRequest.fromLat = 37.798274;
        profileRequest.fromLon = -122.403891;
        profileRequest.toLat = 37.379146;
        profileRequest.toLon = -122.080381;
        profileRequest.accessModes = profileRequest.egressModes = profileRequest.directModes = new QualifiedModeSet("CAR_LYFT,WALK");
        profileRequest.transitModes = new TraverseModeSet("TRANSIT");

        ProfileRouter router = new ProfileRouter(graph, profileRequest);
        ProfileResponse response = router.route();
        router.cleanup(); // destroy routing contexts even when an exception happens

        logger.info("{}", response);
    }

}
