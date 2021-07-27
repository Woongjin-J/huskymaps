package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.DefaultTerm;
import autocomplete.Term;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @see Searcher
 */
public class DefaultSearcher extends Searcher {
    private List<Node> node;
    private List<String> byPrefix;
    private List<Node> byLocation;
    private HashSet<String> organized;

    public DefaultSearcher(StreetMapGraph graph) {
        node = graph.allNodes();
        byPrefix = new ArrayList<>();
        byLocation = new ArrayList<>();
        organized = new HashSet<>();
    }

    @Override
    protected Term createTerm(String name, int weight) {
        return new DefaultTerm(name, weight);
    }

    @Override
    protected Autocomplete createAutocomplete(Term[] termsArray) {
        return new Autocomplete(termsArray);
    }

    @Override
    public List<String> getLocationsByPrefix(String prefix) {
        for (int i = 0; i < node.size(); i++) {
            if (node.get(i).name() != null && node.get(i).name().startsWith(prefix)) {
                organized.add(node.get(i).name());
            }
        }
        byPrefix.addAll(organized);
        return byPrefix;
    }

    @Override
    public List<Node> getLocations(String locationName) {
        for (int i = 0; i < node.size(); i++) {
            if (node.get(i).name().compareTo(locationName) == 0) {
                byLocation.add(node.get(i));
            }
        }
        return byLocation;
    }
}
