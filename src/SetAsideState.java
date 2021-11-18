public class SetAsideState implements InputFileState {
    @Override
    public void extractIdentifier(InputFile inputFile) {
        String identifier = Utils.IdentifierExtractor.getIdentifier(inputFile.getSubmission());
        inputFile.setIdentifier(identifier);

        switch (identifier) {
            case "SET_ASIDE" -> inputFile.setState(new DiscardedState());
//            case "DISCARDED" -> inputFile.setCurrentState(new DiscardedState(inputFile));
            default -> inputFile.setState(new PrimedState());
        }
    }
}