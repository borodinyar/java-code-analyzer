public class MultipleEmptyBlocks {
    void check() {
        if (true) {
            // empty block
        }
        while (false) {
            // empty block
        }
    }
}