package watchout;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by Damiano on 30/04/15.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return FragmentsActivity.class;
    }
}
