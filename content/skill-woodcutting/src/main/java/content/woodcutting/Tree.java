package content.woodcutting;

public enum Tree {
    TREE("sm_env_tree_round_01", "logs"),
    OAK_TREE("sm_env_tree_round_03", "logs"),
    MAPLE_TREE("sm_env_tree_round_01_mat4", "maple_logs");

    private final String treeObjectId;

    private final String logItemId;

    Tree(String treeObjectId, String logItemId) {
        this.treeObjectId = treeObjectId;
        this.logItemId = logItemId;
    }

    public String getTreeObjectId() {
        return treeObjectId;
    }

    public String getLogItemId() {
        return logItemId;
    }
}
