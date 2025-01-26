public class NestedEmptyBlock {
    void check() {
        if (true) {
            if (false) {
                // empty block
            }
        }
    }
}