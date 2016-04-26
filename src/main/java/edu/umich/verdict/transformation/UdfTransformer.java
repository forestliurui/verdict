package edu.umich.verdict.transformation;

import edu.umich.verdict.Configuration;
import edu.umich.verdict.connectors.MetaDataManager;
import edu.umich.verdict.processing.SelectStatement;

public class UdfTransformer extends QueryTransformer {
    public UdfTransformer(Configuration conf, MetaDataManager metaDataManager, SelectStatement q) {
        super(conf, metaDataManager, q);
    }

    @Override
    protected String getUniformTrialExpression(SelectListItem item, int trial) {
        switch (item.getAggregateType()) {
            case AVG:
                return "sum((" + item.getInnerExpression() + ") * " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + "))/" + transformed.getSample().getRowCount() + ")";
            case SUM:
                return "sum((" + item.getInnerExpression() + ") * " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + "))";
            case COUNT:
                return "sum(case when (" + item.getInnerExpression() + ") is null then 0 else " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + ") end)";
            default:
                return null;
        }
    }

    @Override
    protected String getStratifiedTrialExpression(SelectListItem item, int trial) {
        String weightColumn = sampleAlias + "." + metaDataManager.getWeightColumn();
        switch (item.getAggregateType()) {
            case AVG:
                return "sum((" + item.getInnerExpression() + ") * " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + ") * " + weightColumn + ")/" + transformed.getSample().getRowCount() + ")";
            case SUM:
                return "sum((" + item.getInnerExpression() + ") * " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + ") * " + weightColumn + ")";
            case COUNT:
                return "sum(case when (" + item.getInnerExpression() + ") is null then 0 else " + MetaDataManager.METADATA_DATABASE + ".poisson(" + trial + ") * " + weightColumn + " end)";
            default:
                return null;
        }
    }
}
