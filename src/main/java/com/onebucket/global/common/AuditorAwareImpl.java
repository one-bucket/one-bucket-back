package com.onebucket.global.common;

import com.onebucket.global.utils.SecurityUtils;
import kotlin.jvm.internal.SerializedIr;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <br>package name   : com.onebucket.global.common
 * <br>file name      : AuditorAware
 * <br>date           : 2024-07-31
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-31        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final SecurityUtils securityUtils;

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(securityUtils.getCurrentUsername());
    }
}
