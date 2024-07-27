package com.iafenvoy.jupiter.malilib.interfaces;

public interface IConfirmationListener {
    boolean onActionConfirmed();

    boolean onActionCancelled();
}
