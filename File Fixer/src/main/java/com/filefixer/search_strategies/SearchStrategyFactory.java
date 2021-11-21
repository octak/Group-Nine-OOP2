package com.filefixer.search_strategies;

import com.filefixer.Utils;

import java.io.File;

/**
 * Generates an appropriate {@code SearchStrategy} for a given PDF input, based on its naming convention.
 */
public class SearchStrategyFactory {
    /**
     * Provides {@code SearchStrategies}.
     *
     * @param pdf a PDF file
     * @return a {@code SearchStrategy} object based on the naming convention of the PDF file
     */
    public SearchStrategy getSearchStrategy(File pdf) {
        return switch (Utils.naming_convention(pdf)) {
            case 1 -> new NewConventionStrategy();
            case 0 -> new OldConventionStrategy();
            default -> new RandomStrategy();
        };
    }
}
