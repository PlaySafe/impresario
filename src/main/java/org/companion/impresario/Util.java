package org.companion.impresario;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class Util {

    private Util() {
    }

    static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        if (a == null || b == null) {
            return Collections.emptySet();
        }
        Set<T> duplicate = new HashSet<>(b.size());
        for (T x : b) {
            if (a.contains(x)) {
                duplicate.add(x);
            }
        }
        return (duplicate.isEmpty())
                ? Collections.emptySet()
                : Collections.unmodifiableSet(duplicate);
    }

    static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> x = new HashSet<>(a);
        x.addAll(b);
        return x;
    }

    static Map<String, String> allAttributes(Node node) {
        NamedNodeMap attributeNodes = node.getAttributes();
        Map<String, String> attributes = new HashMap<>(attributeNodes.getLength());
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Node attributeNode = attributeNodes.item(i);
            attributes.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
        }
        return attributes;
    }
}
