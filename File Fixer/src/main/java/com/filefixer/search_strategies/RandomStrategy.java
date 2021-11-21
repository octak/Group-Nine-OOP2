package com.filefixer.search_strategies;

import java.io.File;

public class RandomStrategy implements SearchStrategy {
  @Override
  public String getOriginalFilename(File pdf) {
    return pdf.getName();
  }
}
