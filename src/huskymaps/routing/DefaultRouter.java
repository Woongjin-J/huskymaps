package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import pointsets.KDTreePointSet;
import pointsets.Point;
import pointsets.PointSet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static huskymaps.utils.Spatial.projectToPoint;

/**
 * @see Router
 */
public class DefaultRouter extends Router {
    private StreetMapGraph graph;
    private List<NodePoint> nodes;
    private PointSet<NodePoint> set;
    private ShortestPathFinder<Node> path;

    public DefaultRouter(StreetMapGraph graph) {
        this.graph = graph;
        this.nodes = new ArrayList<>();
        this.path = createPathFinder(graph);
        for (Node n : graph.allNodes()) {
            NodePoint nodeP = createNodePoint(n);
            if (!graph.neighbors(n).isEmpty()) {
                nodes.add(nodeP);
            }
        }
        this.set = createPointSet(nodes);
    }

    @Override
    protected <T extends Point> PointSet<T> createPointSet(List<T> points) {
        // uncomment (and import) if you want to use WeirdPointSet instead of your own KDTreePointSet:
        // return new WeirdPointSet<>(points);
        return KDTreePointSet.createAfterShuffling(points);
    }

    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g) {
        return new AStarPathFinder<>(g);
    }

    @Override
    protected NodePoint createNodePoint(Node node) {
        return projectToPoint(Coordinate.fromNode(node), (x, y) -> new NodePoint(x, y, node));
    }

    @Override
    protected Node closest(Coordinate c) {
        // Project to x and y coordinates instead of using raw lat and lon for finding closest points:
        Point p = projectToPoint(c, Point::new);
        return set.nearest(p).node();
    }

    @Override
    public List<Node> shortestPath(Coordinate start, Coordinate end) {
        Node src = closest(start);
        Node dest = closest(end);
        /*
        Feel free to use any arbitrary duration for your path finding timeout; we don't expect
        queries to take more than a few seconds, so e.g. 10-30 seconds is pretty reasonable.
         */
        return path.findShortestPath(src, dest, Duration.ofSeconds(30)).solution();
    }

    @Override
    public List<NavigationDirection> routeDirections(List<Node> route) {
        // Optional
        return null;
    }
}
