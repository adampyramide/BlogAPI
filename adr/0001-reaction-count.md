# 0001 - Reaction Count

- **Status**: Accepted
- **Date**: July 15, 2025

## Context
Blog posts can receive reactions. Each reaction is stored as a separate `Reaction` entity, which includes the blog post ID and the reaction type (like or dislike). We want to expose the total like and dislike counts in the blog post response schemas (both for single and paginated responses).

## Decision
We will calculate like and dislike counts using aggregate queries at request time. For bulk blog post requests (e.g., pagination endpoints), we perform a grouped aggregate query to count reactions per blog post ID efficiently.

## Alternatives Considered
### 1. Store reaction counts in the `BlogPost` entity
Increment or decrement the like/dislike counters whenever a reaction is added, removed, or changed.

**Pros:**
- Very fast reads
- No need for aggregated queries at request time

**Cons:**
- Risk of stale data
- Edge cases with concurrent updates
- More complex and error-prone write logic

### 2. Use a caching layer (e.g., Redis)
Use a caching layer to reduce the amount of aggregated queries if many requests for the same blog post.

**Pros:**
- Fast reads
- Reduces load on the database

**Cons:**
- Adds infrastructure complexity
- Cached counts may become stale
- Requires cache invalidation logic

## Consequences
This approach guarantees accurate like/dislike counts in BlogPost responses with minimal risk of data inconsistency. It adds some performance overhead due to the need for aggregate queries, but this is acceptable at the current traffic level. If traffic increases significantly, switching to an alternative method should be considered.
