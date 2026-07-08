package com.newBie.new_bie.features.profile.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.newBie.new_bie.core.components.TopBarLayout
import com.newBie.new_bie.features.notification.presentation.viewModels.NotificationViewModel
import com.newBie.new_bie.ui.theme.AppTextStyle

@Composable
fun TermsScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
){
    val isRead by notificationViewModel.isRead.collectAsState()

    Scaffold(
        topBar = {
            TopBarLayout(
                title = "약관 개인정보 처리방침",
                navController = navController,
                isRead = isRead
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "개인정보 처리방침\n" +
                            "본 개인정보 처리방침은 YeongTeak Noh가 운영하는 모바일 기기용 /:new_bie 앱 및 관련 서비스(이하 \"애플리케이션\")에 적용됩니다. YeongTeak Noh는 본 방침에서 \"서비스 제공자\"로 지칭됩니다.\n" +
                            "\n" +
                            "정보 수집 및 이용\n" +
                            "애플리케이션은 사용자가 다운로드하고 이용할 때 정보를 수집합니다. 이 정보에는 다음과 같은 내용이 포함될 수 있습니다.\n" +
                            "\n" +
                            "사용자 기기의 인터넷 프로토콜 주소\n" +
                            "사용자가 방문한 애플리케이션 페이지, 방문 시간 및 날짜, 해당 페이지에서 보낸 시간\n" +
                            "애플리케이션에서 보낸 시간\n" +
                            "사용자가 사용하는 모바일 운영 체제\n" +
                            "\n" +
                            "쿠키 및 추적 기술\n" +
                            "애플리케이션 또는 제3자 SDK는 기능, 분석 또는 서비스 제공을 지원하기 위해 쿠키, SDK, 픽셀 및 유사한 기술을 사용할 수 있습니다. 관련 법률에서 요구하는 경우, 서비스 제공자는 필수적이지 않은 추적 기술을 사용하기 전에 동의를 얻습니다.\n" +
                            "\n" +
                            "사용자의 권리\n" +
                            "사용자는 서비스 제공자가 보유한 개인 데이터에 대한 접근, 수정 또는 삭제를 요청할 수 있습니다. 이러한 권리를 행사하거나, 처리 과정이 동의에 기반한 경우 동의를 철회하려면 서비스 제공업체(yt873588@gmail.com)로 문의하십시오.\n" +
                            "\n" +
                            "캘리포니아 개인정보 보호 권리(CCPA/CPRA)\n" +
                            "캘리포니아 거주자는 수집되는 개인정보가 무엇인지 알 권리, 개인정보를 삭제할 권리, 개인정보의 판매 또는 공유를 거부할 권리, 그리고 이러한 권리 행사로 인해 차별받지 않을 권리가 있습니다. CCPA/CPRA 권리를 행사하려면 서비스 제공업체(yt873588@gmail.com)로 문의하십시오.\n" +
                            "\n" +
                            "서비스 제공업체는 귀하가 제공한 정보를 사용하여 중요 정보, 필수 고지 사항, 그리고 법률이 허용하는 경우 마케팅 커뮤니케이션을 전송할 수 있습니다.\n" +
                            "\n" +
                            "더 나은 애플리케이션 사용 경험을 위해 서비스 제공업체는 귀하에게 특정 개인 식별 정보를 제공하도록 요청할 수 있습니다. 서비스 제공업체가 요청하는 정보는 본 개인정보 보호정책에 설명된 대로 보관 및 사용됩니다.\n" +
                            "\n" +
                            "제3자 접근\n" +
                            "집계되고 익명화된 데이터만 서비스 제공업체가 애플리케이션 및 서비스를 개선하는 데 도움을 주기 위해 주기적으로 외부 서비스로 전송됩니다. 서비스 제공업체는 본 개인정보 처리방침에 설명된 방식으로 귀하의 정보를 제3자와 공유할 수 있습니다.\n" +
                            "\n" +
                            "국제 데이터 전송\n" +
                            "서비스 제공업체 또는 제3자 서비스 제공업체는 유럽 경제 지역(EEA) 외부를 포함하여 귀하의 거주 국가 이외의 국가로 개인 데이터를 전송할 수 있습니다. 관련 법률에서 국제 전송에 대한 보호 조치를 요구하는 경우, 서비스 제공업체는 적절한 메커니즘을 사용합니다.\n" +
                            "\n" +
                            "유럽 위원회에서 승인한 표준 계약 조항(SCC)\n" +
                            "적정성 결정 또는 기타 법적으로 인정되는 전송 메커니즘\n" +
                            "필요하고 법적으로 허용되는 경우 귀하의 동의\n" +
                            "다른 국가의 데이터 보호법은 귀하의 관할 지역의 법률과 다를 수 있습니다. 법률에서 요구하는 경우, 서비스 제공업체는 적절한 보호 조치를 적용하고 전송에 필요한 모든 동의를 얻습니다.\n" +
                            "\n" +
                            "서비스 제공업체는 사용자가 제공한 정보 및 자동으로 수집된 정보를 다음과 같은 경우에 공개할 수 있습니다.\n" +
                            "\n" +
                            "소환장 또는 유사한 법적 절차를 준수하는 등 법률에서 요구하는 경우\n" +
                            "\n" +
                            "정보 제공자는 자신의 권리를 보호하거나, 귀하 또는 타인의 안전을 보호하거나, 사기 행위를 조사하거나, 정부 요청에 응하기 위해 정보 공개가 필요하다고 선의로 판단하는 경우 정보를 공개할 수 있습니다.\n" +
                            "\n" +
                            "또한, 정보 제공자를 대신하여 업무를 수행하고, 정보 제공자가 공개한 정보를 독립적으로 사용하지 않으며, 본 개인정보 처리방침에 명시된 규칙을 준수하기로 동의한 신뢰할 수 있는 서비스 제공업체와 정보를 공유할 수 있습니다.\n" +
                            "\n" +
                            "정보 수집 거부 권리\n" +
                            "앱을 삭제하면 귀하의 기기에서 정보 수집을 중단할 수 있습니다. 앱을 삭제하면 더 이상 기기에서 데이터가 수집되지 않지만, 이미 서비스 제공업체 또는 제3자에게 전송된 정보는 자동으로 삭제되지 않습니다.\n" +
                            "\n" +
                            "개인정보 삭제 요청, 동의 철회 또는 기타 권리 행사를 원하시면 yt873588@gmail.com으로 서비스 제공업체에 문의하십시오.\n" +
                            "\n" +
                            "데이터 보존 정책\n" +
                            "서비스 제공자는 명시된 목적 달성에 필요한 경우에 한하여 개인 데이터를 보존합니다.\n" +
                            "\n" +
                            "사용자 제공 데이터: 애플리케이션 사용 기간 및 그 후 12개월 동안 보존하며, 법률에 따라 더 긴 보존 기간이 요구되는 경우는 예외입니다.\n" +
                            "자동 수집 데이터: 수집일로부터 최대 24개월 동안 보존하며, 법률 준수를 위해 더 긴 보존 기간이 요구되는 경우는 예외입니다.\n" +
                            "집계 및 익명화된 데이터: 더 이상 사용자를 식별할 수 없으므로 무기한 보존합니다.\n" +
                            "법률 준수에 필요한 데이터: 관련 법률에서 요구하는 기간 동안 보존합니다.\n" +
                            "사용자는 법적 보존 의무가 있는 경우를 제외하고 개인 데이터 삭제를 요청할 수 있습니다. 애플리케이션을 통해 제출된 사용자 제공 데이터의 삭제를 원하시면 yt873588@gmail.com으로 문의해 주십시오. 일부 사용자 제공 데이터는 서비스 제공을 위해 필요할 수 있습니다.",
                    style = AppTextStyle.Content
                )
            }
        }
    }
}