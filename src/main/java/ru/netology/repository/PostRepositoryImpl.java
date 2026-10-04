package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final List<Post> posts = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public List<Post> all() {
        return posts;
    }

    @Override
    public Optional<Post> getById(long id) {
        return posts.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = counter.incrementAndGet();
            post.setId(newId);
            posts.add(post);
            return post;
        } else {
            var existing = getById(post.getId());
            if (existing.isPresent()) {
                var old = existing.get();
                old.setContent(post.getContent());
                return old;
            } else {
                throw new NotFoundException("Post not found");
            }
        }
    }

    @Override
    public void removeById(long id) {
        posts.removeIf(p -> p.getId() == id);
    }
}
