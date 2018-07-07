package com.ljcr.api.definitions;

public final class OnParentVersionActions {

    public static final class StandardAction implements OnParentVersionAction {
        private final String code;

        public StandardAction(String code) {
            this.code = code;
        }

        @Override
        public String getIdentifier() {
            return code;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof OnParentVersionAction) {
                return code.equals(((OnParentVersionAction) obj).getIdentifier());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }

    public static final OnParentVersionAction COPY = new StandardAction("COPY");
}
