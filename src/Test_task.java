import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;

public class DocumentManager {

    // In-memory storage for documents
    private final Map<String, Document> documentStorage = new HashMap<>();

    public Document save(Document document) {
        if (document.getId() == null) {
            // Generate a unique ID if the document doesn't have one
            String uniqueId = UUID.randomUUID().toString();
            document.setId(uniqueId);
        }

        // Ensure `created` field remains unchanged if the document already exists
        if (documentStorage.containsKey(document.getId())) {
            Document existingDoc = documentStorage.get(document.getId());
            document.setCreated(existingDoc.getCreated());
        } else if (document.getCreated() == null) {
            document.setCreated(Instant.now());
        }

        // Save or update the document
        documentStorage.put(document.getId(), document);
        return document;
    }

    public List<Document> search(SearchRequest request) {
        List<Document> results = new ArrayList<>();

        for (Document document : documentStorage.values()) {
            if (matchesSearchRequest(document, request)) {
                results.add(document);
            }
        }

        return results;
    }

    public Optional<Document> findById(String id) {
        if (documentStorage.containsKey(id)) {
            return Optional.of(documentStorage.get(id));
        }
        return Optional.empty();
    }

    private boolean matchesSearchRequest(Document document, SearchRequest request) {
        if (request.getTitlePrefixes() != null && !request.getTitlePrefixes().isEmpty()) {
            boolean matchesPrefix = false;
            for (String prefix : request.getTitlePrefixes()) {
                if (document.getTitle().startsWith(prefix)) {
                    matchesPrefix = true;
                    break;
                }
            }
            if (!matchesPrefix) {
                return false;
            }
        }

        if (request.getContainsContents() != null && !request.getContainsContents().isEmpty()) {
            boolean matchesContent = false;
            for (String content : request.getContainsContents()) {
                if (document.getContent().contains(content)) {
                    matchesContent = true;
                    break;
                }
            }
            if (!matchesContent) {
                return false;
            }
        }

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            if (document.getAuthor() == null || !request.getAuthorIds().contains(document.getAuthor().getId())) {
                return false;
            }
        }

        if (request.getCreatedFrom() != null && document.getCreated().isBefore(request.getCreatedFrom())) {
            return false;
        }

        if (request.getCreatedTo() != null && document.getCreated().isAfter(request.getCreatedTo())) {
            return false;
        }

        return true;
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}
