# DocumentManager

The **DocumentManager** is a simple in-memory storage system for managing documents with the ability to save, search, and retrieve documents based on various criteria.

## Features

- **Save Documents**:
    - Adds new documents or updates existing ones.
    - Automatically generates a unique ID for new documents.
    - Maintains the original `created` timestamp for existing documents.

- **Search Documents**:
    - Search by title prefixes, content substrings, author IDs, and creation timestamps.

- **Retrieve Documents by ID**:
    - Finds a document by its unique ID.

## Classes and Structure

### 1. `DocumentManager`
- **Methods**:
    - `save(Document document)`: Saves or updates a document.
    - `search(SearchRequest request)`: Searches for documents based on given criteria.
    - `findById(String id)`: Retrieves a document by its ID.

- **Internal Logic**:
    - Uses a `HashMap` to store documents for efficient retrieval.
    - Matches documents to search criteria using the `matchesSearchRequest` helper method.

### 2. `Document`
- Represents a document with the following fields:
    - `id`: Unique identifier.
    - `title`: Title of the document.
    - `content`: Main content of the document.
    - `author`: Associated author.
    - `created`: Timestamp of creation.

### 3. `Author`
- Represents an author with the following fields:
    - `id`: Unique identifier for the author.
    - `name`: Author's name.

### 4. `SearchRequest`
- Represents search criteria with the following fields:
    - `titlePrefixes`: List of title prefixes to search for.
    - `containsContents`: List of substrings to search in document content.
    - `authorIds`: List of author IDs to match.
    - `createdFrom`: Start of the creation timestamp range.
    - `createdTo`: End of the creation timestamp range.

## Usage

### Example Code

```java
public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        // Create and save documents
        Document doc1 = Document.builder()
                .title("Java Basics")
                .content("Learn about Java Streams and Collections.")
                .author(Author.builder().id("1").name("John Doe").build())
                .build();
        manager.save(doc1);

        Document doc2 = Document.builder()
                .title("Advanced Java")
                .content("Understand lambdas and functional programming.")
                .author(Author.builder().id("2").name("Jane Doe").build())
                .build();
        manager.save(doc2);

        // Search for documents
        SearchRequest request = SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Java"))
                .containsContents(Arrays.asList("Streams"))
                .build();
        List<Document> results = manager.search(request);

        // Display results
        for (Document doc : results) {
            System.out.println("Found Document: " + doc);
        }

        // Find by ID
        Optional<Document> foundDoc = manager.findById(doc1.getId());
        foundDoc.ifPresent(document -> System.out.println("Found by ID: " + document));
    }
}
